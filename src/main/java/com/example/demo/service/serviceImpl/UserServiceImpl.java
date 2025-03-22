package com.example.demo.service.serviceImpl;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.models.entity.constant.Role;
import com.example.demo.repository.constant.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ApplicationConstants;
import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.SignUpRequestObject;
import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.VerifyOtpRequestObject;
import com.example.demo.models.entity.master.User;
import com.example.demo.repository.master.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtils;

import static com.example.demo.constants.ApplicationConstants.ErrorMessage.INVALID_CREDENTIALS;
import static com.example.demo.constants.ApplicationConstants.ErrorMessage.UNMATCHED_OTP;
import static com.example.demo.constants.ApplicationConstants.ErrorMessage.USER_NOT_FOUND;
import static com.example.demo.constants.ApplicationConstants.SuccessMessage.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final SendSMS sendSMS;
    private final RoleRepository roleRepository;

    @Value("${jwt.expiration}")
    private String jwtExpirationStr;

    private long jwtExpiration;

    @PostConstruct
    public void init() {
        jwtExpiration = Long.parseLong(jwtExpirationStr); // Convert String to Long
    }

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           JwtService jwtService,
                           SendSMS sendSMS, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.sendSMS = sendSMS;
        this.roleRepository = roleRepository;
    }

    private String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * <p>
     * This method registers the user for the first time.
     * Validates if the user is already present and send the user details
     * Else OTP is sent to user and saved in database
     * </p>
     *
     * @param signUpRequestObject {@link SignUpRequestObject}
     * @return commonResponse containing user details
     */
    public CommonResponse<User> register(SignUpRequestObject signUpRequestObject) {
        LOGGER.debug("In UserServiceImpl::register for email {}", signUpRequestObject.getEmail());

        String message;
        Optional<User> optionalUser = userRepository.findByMobileNumber(signUpRequestObject.getMobileNumber());
        User user;

        // validate if the user already exists
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            message = ApplicationConstants.SuccessMessage.USER_ALREADY_EXISTS;
        } else {
            int otp = CommonUtils.generateOTP();
            Set<Role> role = new HashSet<>();
            roleRepository.findById(Role.RoleValues.MERCHANT.getId()).ifPresent(role::add);

            // save user details
            user = User.builder()
                    .email(signUpRequestObject.getEmail())
                    .password(bCryptPasswordEncoder.encode(signUpRequestObject.getPassword()))
                    .mobileNumber(signUpRequestObject.getMobileNumber())
                    .name(signUpRequestObject.getName())
                    .otp(String.valueOf(otp))
                    .isActive(2)
                    .roles(role)
                    .build();

            // call external service to send OTP to mobile number
            sendSMS.sendSms(signUpRequestObject.getMobileNumber(), otp);
            user = userRepository.save(user);
            message = ApplicationConstants.SuccessMessage.USER_REGISTERED;
        }

        user.setOtp(null);
        user.setCreatedAt(null);
        user.setModifiedAt(null);
        LOGGER.debug("Out UserServiceImpl::register for email {}", signUpRequestObject.getEmail());
        return new CommonResponse<>(user, message);
    }

    /**
     * This method verifies the OTP for the user sent from the request
     *
     * @param verifyOtpRequestObject {@link VerifyOtpRequestObject} Object containing userID and otp
     * @return commonResponse containing user details
     */
    public CommonResponse<Map<String, Object>> verify(VerifyOtpRequestObject verifyOtpRequestObject) {
        LOGGER.debug("In UserServiceImpl::verify");
        Optional<User> userOptional = userRepository.findById(verifyOtpRequestObject.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        User user = userOptional.get();
        Set<Role> roles = user.getRoles();
        if (CommonUtils.checkValueAbsent(user.getOtp(), String.valueOf(verifyOtpRequestObject.getOtp()))) {
            throw new IllegalArgumentException(UNMATCHED_OTP);
        }
        String authenticationToken = jwtService.generateToken(user.getMobileNumber(),roles.stream().map(Role::getName).collect(Collectors.toList()));
        user.setLoginToken(authenticationToken);
        user = userRepository.save(user);
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("token", authenticationToken);
        responseMap.put("expires_in", System.currentTimeMillis() + jwtExpiration);
        responseMap.put("expires_at", new Date(System.currentTimeMillis() + jwtExpiration));
        responseMap.put("user", user);

        LOGGER.debug("Out UserServiceImpl::verify");
        return new CommonResponse<>(responseMap, ApplicationConstants.SuccessMessage.USER_VERIFIED);
    }

    /**
     * This method logs in based on email or phone number.
     *
     * @param signinRequest {@link SigninRequest}
     * @return commonResponse containing token and user details after the user gets loggedIn
     */
    public CommonResponse<Map<String, Object>> loginByEmailOrPhone(SigninRequest signinRequest) {
        LOGGER.debug("In UserServiceImpl::loginByEmailOrPhone for user-identification {}", signinRequest.getUserIdentification());
        HashMap<String, Object> responseMap = new HashMap<>();
        // fetch the user details
        Optional<User> userOptional = userRepository.findByEmailOrMobileNumber(signinRequest.getUserIdentification(), signinRequest.getUserIdentification());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }

        // check if the user is present else throw an exception
        User user = userOptional.get();
        Set<Role> roles = user.getRoles();

        // validate if the password matches
        if (!bCryptPasswordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }

        // prepare response
        responseMap.put("token", jwtService.generateToken(user.getMobileNumber(),roles.stream().map(Role::getName).collect(Collectors.toList())));
        responseMap.put("expires_in", System.currentTimeMillis() + jwtExpiration);
        responseMap.put("expires_at", new Date(System.currentTimeMillis() + jwtExpiration));
        responseMap.put("user", user);
        LOGGER.debug("Out UserServiceImpl::loginByEmailOrPhone");
        return new CommonResponse<>(responseMap, USER_LOGIN);
    }

    public CommonResponse<String> resendOtp(int userId) {
        LOGGER.debug("In UserServiceImpl::resendOtp for user-identification {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        int otp = CommonUtils.generateOTP();
        User user = userOptional.get();
        user.setOtp(String.valueOf(otp));
        sendSMS.sendSms(user.getMobileNumber(), otp);
        userRepository.save(user);
        LOGGER.debug("Out UserServiceImpl::resendOtp for user-identification {}", userId);
        return new CommonResponse<>(OTP_SENT_AGAIN);
    }

    @Override
    public CommonResponse<List<String>> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.debug("In UserServiceImpl::getRoles ");
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        LOGGER.debug("Out UserServiceImpl::getRoles");
        return new CommonResponse<>(roles, ROLES_FETCHED_SUCCESSFULLY);
    }

    public CommonResponse<User> resetPassword(int userId, String password) {
        LOGGER.debug("In UserServiceImpl::resetPassword for user-identification {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        User user = userOptional.get();
        user.setPassword(encryptPassword(password));
        user = userRepository.save(user);
        LOGGER.debug("Out UserServiceImpl::resetPassword");
        return new CommonResponse<>(user, PASSWORD_SET_SUCCESSFULLY);
    }
}
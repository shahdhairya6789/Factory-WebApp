package org.example.service.serviceImpl;


import org.example.dto.SigninRequest;
import org.example.dto.SignupRequest;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    public String register(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        User user = mapToUserForSignup(signupRequest);
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String verify(SigninRequest signinRequest) {
        Authentication authenticate
                = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(), signinRequest.getPassword()
                )
        );

        //var u = userRepository.findByUserName(user.getUserName());
        User user = mapToUserForSignin(signinRequest);
        if(authenticate.isAuthenticated())
            return jwtService.generateToken(user);
        throw new IllegalArgumentException("Invalid username or password!");
    }

    User mapToUserForSignup(SignupRequest signupRequest) {
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .build();
        return user;
    }

    User mapToUserForSignin(SigninRequest signinRequest) {
        User user = User.builder()
                .email(signinRequest.getEmail())
                .password(signinRequest.getPassword())
                .build();
        return user;
    }
}

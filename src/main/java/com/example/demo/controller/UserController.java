package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.ResetPassword;
import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.SignUpRequestObject;
import com.example.demo.models.dto.VerifyOtpRequestObject;
import com.example.demo.models.entity.master.User;
import com.example.demo.service.UserService;

@RestController
//@RequestMapping("")
//@CrossOrigin(origins = "http://localhost:5173")  // Frontend URL
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @PostMapping("/auth/register")
    public CommonResponse<User> register(@RequestBody SignUpRequestObject user) {
        LOGGER.debug("In UserController::register for email {}", user.getEmail());
        CommonResponse<User> commonResponse = userService.register(user);
        LOGGER.debug("Out UserController::register");
        return commonResponse;
    }

//    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @PostMapping("/auth/login")
    public CommonResponse<Map<String, Object>> login(HttpServletRequest httpServletRequest,
                                                     Principal principal,
                                                     @RequestBody SigninRequest user) {
        LOGGER.debug("In UserController::login for user-identification {}", user.getUserIdentification());
        CommonResponse<Map<String, Object>> commonResponse = userService.loginByEmailOrPhone(user);
        LOGGER.debug("Out UserController::login");
        return commonResponse;
    }

    @PostMapping("/auth/verify-otp")
    public CommonResponse<Map<String, Object>> verifyOtp(@RequestBody VerifyOtpRequestObject verifyOtpRequestObject) {
        LOGGER.debug("In UserController::verifyOtp for userID {}", verifyOtpRequestObject.getUserId());
        CommonResponse<Map<String, Object>> commonResponse = userService.verify(verifyOtpRequestObject);
        LOGGER.debug("Out UserController::verifyOtp");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @GetMapping("/v1/users/{userId}/resend-otp")
    public CommonResponse<String> resendOtp(@PathVariable("userId") int userId){
        LOGGER.debug("In UserController::resendOtp for userID {}", userId);
        CommonResponse<String> commonResponse = userService.resendOtp(userId);
        LOGGER.debug("Out UserController::resendOtp");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @PostMapping("/v1/users/reset-password")
    public CommonResponse<User> resetPassword(@RequestBody ResetPassword resetPassword) {
        LOGGER.debug("In UserController::resetPassword for userID {}", resetPassword.getUserId());
        CommonResponse<User> commonResponse = userService.resetPassword(resetPassword.getUserId(), resetPassword.getPassword());
        LOGGER.debug("Out UserController::resetPassword");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('merchant', 'admin', 'employee', '')")
    @GetMapping("/v1/users/roles")
    public CommonResponse<List<String>> getRoles() {

        LOGGER.debug("In UserController::getRoles");
        CommonResponse<List<String>> commonResponse = userService.getRoles();
        LOGGER.debug("Out UserController::getRoles");
        return commonResponse;
    }

}

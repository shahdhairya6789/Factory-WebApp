package com.example.demo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.ResetPassword;
import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.SignUpRequestObject;
import com.example.demo.models.dto.VerifyOtpRequestObject;
import com.example.demo.models.entity.master.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public CommonResponse<User> register(@RequestBody SignUpRequestObject user) {
        LOGGER.debug("In UserController::register for email {}", user.getEmail());
        CommonResponse<User> commonResponse = userService.register(user);
        LOGGER.debug("Out UserController::register");
        return commonResponse;
    }

    @PostMapping("/login")
    public CommonResponse<Map<String, Object>> login(@RequestBody SigninRequest user) {
        LOGGER.debug("In UserController::login for user-identification {}", user.getUserIdentification());
        CommonResponse<Map<String, Object>> commonResponse = userService.loginByEmailOrPhone(user);
        LOGGER.debug("Out UserController::login");
        return commonResponse;
    }

    @PostMapping("/verify-otp")
    public CommonResponse<User> verifyOtp(@RequestBody VerifyOtpRequestObject verifyOtpRequestObject) {
        LOGGER.debug("In UserController::verifyOtp for userID {}", verifyOtpRequestObject.getUserId());
        CommonResponse<User> commonResponse = userService.verify(verifyOtpRequestObject);
        LOGGER.debug("Out UserController::verifyOtp");
        return commonResponse;
    }

    @GetMapping("/{userId}/resend-otp")
    public CommonResponse<String> resendOtp(@PathVariable("userId") int userId){
        LOGGER.debug("In UserController::resendOtp for userID {}", userId);
        CommonResponse<String> commonResponse = userService.resendOtp(userId);
        LOGGER.debug("Out UserController::resendOtp");
        return commonResponse;
    }

    @PostMapping("/reset-password")
    public CommonResponse<User> resetPassword(@RequestBody ResetPassword resetPassword) {
        LOGGER.debug("In UserController::resetPassword for userID {}", resetPassword.getUserId());
        CommonResponse<User> commonResponse = userService.resetPassword(resetPassword.getUserId(), resetPassword.getPassword());
        LOGGER.debug("Out UserController::resetPassword");
        return commonResponse;
    }

}

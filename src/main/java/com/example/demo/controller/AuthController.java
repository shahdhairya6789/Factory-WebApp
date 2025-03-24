package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @PostMapping("/auth/login")
    public CommonResponse<Map<String, Object>> login(HttpServletRequest httpServletRequest,
                                                     Principal principal,
                                                     @RequestBody SigninRequest user) {
        LOGGER.debug("In AuthController::login for user-identification {}", user.getUserIdentification());
        CommonResponse<Map<String, Object>> commonResponse = userService.loginByEmailOrPhone(user);
        LOGGER.debug("Out AuthController::login");
        return commonResponse;
    }

    @PostMapping("/auth/verify-otp")
    public CommonResponse<Map<String, Object>> verifyOtp(@RequestBody VerifyOtpRequestObject verifyOtpRequestObject) {
        LOGGER.debug("In AuthController::verifyOtp for userID {}", verifyOtpRequestObject.getUserId());
        CommonResponse<Map<String, Object>> commonResponse = userService.verify(verifyOtpRequestObject);
        LOGGER.debug("Out AuthController::verifyOtp");
        return commonResponse;
    }

}

package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.ResetPassword;
import com.example.demo.models.dto.SignUpRequestObject;
import com.example.demo.models.dto.ValidUsername;
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

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @GetMapping("/{userId}/resend-otp")
    public CommonResponse<String> resendOtp(@PathVariable("userId") int userId){
        LOGGER.debug("In AuthController::resendOtp for userID {}", userId);
        CommonResponse<String> commonResponse = userService.resendOtp(userId);
        LOGGER.debug("Out AuthController::resendOtp");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE', '')")
    @PostMapping("/reset-password")
    public CommonResponse<User> resetPassword(@RequestBody ResetPassword resetPassword) {
        LOGGER.debug("In AuthController::resetPassword for userID {}", resetPassword.getUserId());
        CommonResponse<User> commonResponse = userService.resetPassword(resetPassword.getUserId(), resetPassword.getPassword());
        LOGGER.debug("Out AuthController::resetPassword");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('merchant', 'admin', 'employee', '')")
    @GetMapping("/roles")
    public CommonResponse<List<String>> getRoles() {

        LOGGER.debug("In AuthController::getRoles");
        CommonResponse<List<String>> commonResponse = userService.getRoles();
        LOGGER.debug("Out AuthController::getRoles");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    @PostMapping
    public CommonResponse<User> register(@RequestBody SignUpRequestObject user) {
        LOGGER.debug("In UserController::register for email {}", user.getEmail());
        CommonResponse<User> commonResponse = userService.register(user);
        LOGGER.debug("Out UserController::register");
        return commonResponse;
    }

    // TODO: Create new endpoint for updating the manager
    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    @PatchMapping
    public CommonResponse<User> updateUserDetails(@RequestBody SignUpRequestObject user) {
        LOGGER.debug("In UserController::updateUserDetails for email {}", user.getEmail());
        CommonResponse<User> commonResponse = userService.register(user);
        LOGGER.debug("Out UserController::updateUserDetails");
        return commonResponse;
    }

//    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    @PatchMapping("/{userId}")
    public CommonResponse<String> deleteUser(@PathVariable int userId) {
        LOGGER.debug("In UserController::deleteUser for userId {}", userId);
        CommonResponse<String> commonResponse = userService.deleteUser(userId);
        LOGGER.debug("Out UserController::deleteUser");
        return commonResponse;
    }

    // TODO: Create new endpoint for fetching details under the manager
//    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    @GetMapping("/employees/{managerId}")
    public CommonResponse<List<User>> getUsersByManagerId(@PathVariable int managerId) {
        LOGGER.debug("In UserController::getUsers: {}", managerId);
        CommonResponse<List<User>> commonResponse = userService.getUserByManagerId(managerId);
        LOGGER.debug("Out UserController::getUsers: {}", managerId);
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    @GetMapping("/valid-username")
    public CommonResponse<ValidUsername> validateUserName(@RequestParam String username) {
        LOGGER.debug("In UserController::validateUserName for username {}", username);
        CommonResponse<ValidUsername> commonResponse = userService.validUsername(username);
        LOGGER.debug("Out UserController::validateUserName");
        return commonResponse;
    }


}

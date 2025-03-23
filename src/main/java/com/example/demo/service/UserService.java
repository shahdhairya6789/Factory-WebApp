package com.example.demo.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.SignUpRequestObject;
import com.example.demo.models.dto.VerifyOtpRequestObject;
import com.example.demo.models.entity.master.User;
import org.springframework.security.core.GrantedAuthority;

public interface UserService {

    CommonResponse<User> register(SignUpRequestObject user);

    CommonResponse<Map<String, Object>> loginByEmailOrPhone(SigninRequest user);

    CommonResponse<Map<String, Object>> verify(VerifyOtpRequestObject verifyOtpRequestObject);

    CommonResponse<User> resetPassword(int userId, String password);

    CommonResponse<String> resendOtp(int userId);

    CommonResponse<List<String>> getRoles();

    CommonResponse<List<User>> getUsers();

    CommonResponse<String> deleteUser(int userId);
}

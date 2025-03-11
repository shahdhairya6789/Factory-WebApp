package com.example.demo.service;

import java.util.Map;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.SignUpRequestObject;
import com.example.demo.models.dto.VerifyOtpRequestObject;
import com.example.demo.models.entity.master.User;

public interface UserService {

    CommonResponse<User> register(SignUpRequestObject user);

    CommonResponse<Map<String, Object>> loginByEmailOrPhone(SigninRequest user);

    CommonResponse<User> verify(VerifyOtpRequestObject verifyOtpRequestObject);

    CommonResponse<User> resetPassword(int userId, String password);

    CommonResponse<String> resendOtp(int userId);
}

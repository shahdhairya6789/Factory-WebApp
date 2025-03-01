package com.example.demo.service;

import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.SignupRequest;

public interface UserService {

    public String register(SignupRequest user);

    public String verify(SigninRequest user);
}

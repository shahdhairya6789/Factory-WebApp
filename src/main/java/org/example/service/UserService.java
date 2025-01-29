package org.example.service;

import org.example.dto.SigninRequest;
import org.example.dto.SignupRequest;
import org.example.entity.User;

public interface UserService {

    public String register(SignupRequest user);

    public String verify(SigninRequest user);

}

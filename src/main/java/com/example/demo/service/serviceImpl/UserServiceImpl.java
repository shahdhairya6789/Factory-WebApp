package com.example.demo.service.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.dto.SigninRequest;
import com.example.demo.models.dto.SignupRequest;
import com.example.demo.models.entity.master.User;
import com.example.demo.repository.master.UserRepository;
import com.example.demo.service.UserService;

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
        return User.builder()
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getFirstName() + " " +signupRequest.getLastName())
                .build();
    }

    User mapToUserForSignin(SigninRequest signinRequest) {
        return User.builder()
                .email(signinRequest.getEmail())
                .password(signinRequest.getPassword())
                .build();
    }
}
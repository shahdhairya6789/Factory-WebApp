package com.example.demo.service.serviceImpl;

import com.example.demo.models.entity.master.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.constants.ApplicationConstants;
import com.example.demo.models.entity.constant.Role;
import com.example.demo.models.entity.mapping.UserRole;
import com.example.demo.models.entity.master.User;
import com.example.demo.repository.mapping.UserRoleRepository;
import com.example.demo.repository.master.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        // Need to check how to throw exception if the user by mobileNumber doesn't exist
        Optional<User> user = userRepository.findByMobileNumber(mobileNumber);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(ApplicationConstants.ErrorMessage.USER_NOT_FOUND);
        }

        List<GrantedAuthority> authorities = user.get().getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user.get());
    }
}

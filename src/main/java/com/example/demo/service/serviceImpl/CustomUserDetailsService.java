package com.example.demo.service.serviceImpl;

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
import com.example.demo.util.CustomUserDetails;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        // Need to check how to throw exception if the user by mobileNumber doesn't exist
        Optional<User> user = userRepository.findByMobileNumber(mobileNumber);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(ApplicationConstants.ErrorMessage.USER_NOT_FOUND);
        }
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.get().getId());

        List<GrantedAuthority> authorities = userRoles
                .stream()
                .map(userRole -> new SimpleGrantedAuthority(Role.RoleValues.fetchById(userRole.getRole().getId()).toString()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.get().getMobileNumber(), user.get().getPassword(), authorities);
    }
}

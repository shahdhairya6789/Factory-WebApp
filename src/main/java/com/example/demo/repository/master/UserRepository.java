package com.example.demo.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.master.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrMobileNumber(String email, String mobileNumber);

    Optional<User> findByMobileNumber(String mobileNumber);
    
    List<User> findByManagerId(int managerId);

    boolean existsByMobileNumberAndIsActive(String mobileNumber, boolean isActive);
}

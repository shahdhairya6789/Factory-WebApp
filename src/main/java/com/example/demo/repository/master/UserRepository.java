package com.example.demo.repository.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.master.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

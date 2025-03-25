package com.example.demo.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.mapping.Salary;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    List<Salary> findByUserId(int userId);
}

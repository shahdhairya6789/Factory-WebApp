package com.example.demo.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.mapping.AdvanceSalary;

@Repository
public interface AdvanceSalaryRepository extends JpaRepository<AdvanceSalary, Integer> {
}

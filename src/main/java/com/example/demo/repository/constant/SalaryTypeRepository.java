package com.example.demo.repository.constant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.constant.SalaryType;

@Repository
public interface SalaryTypeRepository extends JpaRepository<SalaryType, Integer> {
}

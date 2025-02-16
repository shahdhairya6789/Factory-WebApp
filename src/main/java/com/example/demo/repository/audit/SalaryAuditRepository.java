package com.example.demo.repository.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.audit.SalaryAudit;

@Repository
public interface SalaryAuditRepository extends JpaRepository<SalaryAudit, Integer> {
}

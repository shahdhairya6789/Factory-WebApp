package com.example.demo.models.entity.mapping;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

import com.example.demo.models.entity.base.AuditColumns;
import com.example.demo.models.entity.constant.SalaryType;
import com.example.demo.models.entity.master.User;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tblt_user_salary_mapping")
public class Salary extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int salary;
    private Timestamp salaryDate;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "salary_type_id", referencedColumnName = "id")
    private SalaryType salaryType;
}

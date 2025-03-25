package com.example.demo.models.entity.mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import com.example.demo.models.entity.base.AuditColumns;
import com.example.demo.models.entity.constant.SalaryType;
import com.example.demo.models.entity.master.User;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tblt_user_salary_mapping")
@NoArgsConstructor
public class Salary extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int salary;
    private Timestamp salaryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private User user;
    @ManyToOne
    @JoinColumn(name = "salary_type_id", referencedColumnName = "id")
    private SalaryType salaryType;

    private int createdBy;
    private int modifiedBy;

    public Salary(int salary, Timestamp salaryDate, SalaryType salaryType, int createdBy, int modifiedBy) {
        this.salary = salary;
        this.salaryDate = salaryDate;
        this.salaryType = salaryType;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }
}

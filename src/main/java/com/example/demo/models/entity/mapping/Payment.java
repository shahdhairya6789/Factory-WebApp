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
@Entity(name = "tblt_user_payment_mapping")
public class Payment extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long advancePayment = 0L;
    private Long paymentAmount;
    private Timestamp paymentDate;
    private Integer workingDays;
    private boolean isActive;
    @ManyToOne
    @JoinColumn(name = "salary_type_id")
    private SalaryType salaryType;
    private int createdBy;
    private int modifiedBy;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

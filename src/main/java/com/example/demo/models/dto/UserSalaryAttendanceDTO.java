package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSalaryAttendanceDTO {
    private int userId;
    private int salaryTypeId;
    private double perDaySalary;
    private int workingDays;
    private double payment;
}

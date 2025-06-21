package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSalaryAttendanceDTO {
    private Integer userId;
    private Integer salaryTypeId;
    private Integer monthlySalary;
    private Long workingDays;
}

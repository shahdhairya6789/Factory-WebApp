package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

import com.example.demo.models.entity.mapping.AdvanceSalary;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvanceSalaryDTO {
    private int userId;
    private int advanceSalaryAmount;
    private int paidByUserId;
    private Timestamp advanceSalaryDate;
    private String userName;
    private String paidByUserName;

    public AdvanceSalaryDTO(AdvanceSalary advanceSalary) {
        this.userId = advanceSalary.getUser().getId();
        this.advanceSalaryAmount = advanceSalary.getAdvanceSalary();
        this.advanceSalaryDate = advanceSalary.getAdvanceSalaryDate();
        this.paidByUserId = advanceSalary.getUser().getId();
        this.userName = advanceSalary.getUser().getName();
        this.paidByUserName = advanceSalary.getUser().getName();
    }
}

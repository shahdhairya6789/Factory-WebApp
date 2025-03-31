package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import com.example.demo.models.entity.mapping.AdvanceSalary;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvanceSalaryDTO {
    private int userId;
    private int advanceSalaryAmount;
    private int paidByUserId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long advanceSalaryDate = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String paidByUserName = null;

    public AdvanceSalaryDTO(int userId, int advanceSalaryAmount, int paidByUserId) {
        this.userId = userId;
        this.advanceSalaryAmount = advanceSalaryAmount;
        this.paidByUserId = paidByUserId;
    }

    public AdvanceSalaryDTO(AdvanceSalary advanceSalary) {
        this.userId = advanceSalary.getUser().getId();
        this.advanceSalaryAmount = advanceSalary.getAdvanceSalary();
        this.advanceSalaryDate = advanceSalary.getAdvanceSalaryDate().getTime();
        this.paidByUserId = advanceSalary.getUser().getId();
        this.userName = advanceSalary.getUser().getName();
        this.paidByUserName = advanceSalary.getUser().getName();
    }
}

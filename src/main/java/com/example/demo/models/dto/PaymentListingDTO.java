package com.example.demo.models.dto;

public interface PaymentListingDTO {
    Long getUserId();
    Double getTotalPayableAmount();
    Double getTotalAdvancePaid();
    Double getNetPayable();
    Integer getSingleDayWorkingCount();
    Integer getDoubleDayWorkingCount();
    Double getSingleMachineSalary();
    Double getDoubleMachineSalary();
}

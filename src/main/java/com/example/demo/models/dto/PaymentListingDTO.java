package com.example.demo.models.dto;

import java.sql.Timestamp;

public interface PaymentListingDTO {
    Long getUserId();
    String getUserName();
    Double getTotalPayableAmount();
    Double getTotalAdvancePaid();
    Double getNetPayable();
    Integer getSingleDayWorkingCount();
    Integer getDoubleDayWorkingCount();
    Double getSingleMachineSalary();
    Double getDoubleMachineSalary();
    Timestamp getPaymentDate();
}

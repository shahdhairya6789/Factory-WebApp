package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class PaymentDTO {
    private int paymentId;
    private int userId;
    private String userName;
    private long advancePayment;
    private long paymentAmount;
    private Timestamp paymentDate;
    private int workingDays;
}

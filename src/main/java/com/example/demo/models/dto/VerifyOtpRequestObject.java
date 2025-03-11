package com.example.demo.models.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.demo.constants.ApplicationConstants.ValidationMessage.INVALID_OTP;
import static com.example.demo.constants.ApplicationConstants.ValidationMessage.INVALID_USER_ID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequestObject {
    @NotNull(message = INVALID_USER_ID)
    private Integer userId;
    @NotNull(message = INVALID_OTP)
    private Integer otp;
}

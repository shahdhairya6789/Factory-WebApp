package com.example.demo.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.demo.constants.ApplicationConstants.ValidationMessage.EMAIL_RESTRICTION_MSG;
import static com.example.demo.constants.ApplicationConstants.ValidationMessage.NAME_REQUIRED_MSG;
import static com.example.demo.constants.ApplicationConstants.ValidationMessage.PASSWORD_RESTRICTION_MSG;
import static com.example.demo.constants.ApplicationConstants.ValidationMessage.PHONE_RESTRICTION_MSG;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestObject {
    @NotNull(message = NAME_REQUIRED_MSG)
    private String name;
    @NotNull
    @Size(min = 10, max = 10, message = PHONE_RESTRICTION_MSG)
    private String mobileNumber;
    @NotNull
    @Email(message = EMAIL_RESTRICTION_MSG)
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = PASSWORD_RESTRICTION_MSG)
    private String password;
}

package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidUsername {
    private boolean userNameExists;
}

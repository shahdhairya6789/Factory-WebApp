package com.example.demo.models.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.example.demo.constants.ApplicationConstants.ValidationMessage.MACHINE_AREA_LENGTH_MESSAGE;
import static com.example.demo.constants.ApplicationConstants.ValidationMessage.MACHINE_HEAD_LENGTH_MESSAGE;
import static com.example.demo.constants.ApplicationConstants.ValidationMessage.MACHINE_NAME_LENGTH_MESSAGE;

@Data
public class CreateMachineVO {
    private int id;
    @Size(min = 1, max = 255, message = MACHINE_NAME_LENGTH_MESSAGE)
    private String name;
    @Size(min = 1, max = 255, message = MACHINE_HEAD_LENGTH_MESSAGE)
    private String heads;
    @Size(min = 1, max = 255, message = MACHINE_AREA_LENGTH_MESSAGE)
    private String area;
}

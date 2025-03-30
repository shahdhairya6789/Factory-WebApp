package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class AttendanceDetailsDTO {
    private int attendanceId;
    private Timestamp attendanceDate;
    private int production;
    private int dhaga;
    private int frames;
    private int userId;
    private String userName;
    private int shiftId;
    private String shiftName;
    private int salaryTypeId;
    private String salaryType;
    private int machineId;
    private String machineName;
}

package com.example.demo.models.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AttendanceVO {
    private int userId;
    private int machineId;
    private int salaryTypeId;
    private String production;
    private String dhaga;
    private int shiftId;
    private int attendanceDate;
}

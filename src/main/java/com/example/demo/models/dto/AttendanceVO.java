package com.example.demo.models.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AttendanceVO {
    private int userId;
    private int machineId;
    private int production;
    private int dhaga;
    private int frames;
    private int shiftId;
    private int attendanceDate;
}

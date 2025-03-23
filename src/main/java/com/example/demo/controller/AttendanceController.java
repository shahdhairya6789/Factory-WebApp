package com.example.demo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.AttendanceVO;
import com.example.demo.service.serviceImpl.AttendanceService;

@RestController
@RequestMapping("/v1/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final Logger LOGGER = LoggerFactory.getLogger(AttendanceController.class);

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PreAuthorize("hasAnyAuthority('Merchant', 'Employee')")
    @PostMapping(consumes = "multipart/form-data")
    public CommonResponse<String> addAttendance(@RequestPart("attendance") AttendanceVO attendance,
                                        @RequestPart("file") MultipartFile file) throws Exception {
        LOGGER.info("In AttendanceController addAttendance");
        CommonResponse<String> commonResponse = attendanceService.addAttendance(file, attendance);
        LOGGER.info("In AttendanceController addAttendance");
        return commonResponse;
    }
}

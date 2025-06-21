package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.AttendanceDetailsDTO;
import com.example.demo.models.dto.AttendanceVO;
import com.example.demo.models.entity.master.Attendance;
import com.example.demo.service.serviceImpl.AttendanceService;

@RestController
@RequestMapping("/v1/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final Logger LOGGER = LoggerFactory.getLogger(AttendanceController.class);

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

        @PreAuthorize("hasAnyAuthority('MERCHANT', 'EMPLOYEE')")
    @PostMapping(consumes = "multipart/form-data")
    public CommonResponse<Attendance> addAttendance(@RequestPart("attendance") AttendanceVO attendance,
                                                    @RequestPart("file") MultipartFile file) throws Exception {
        LOGGER.info("In AttendanceController addAttendance");
        CommonResponse<Attendance> commonResponse = attendanceService.addAttendance(file, attendance);
        LOGGER.info("In AttendanceController addAttendance");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'EMPLOYEE', 'ADMIN')")
    @GetMapping
    public CommonResponse<List<AttendanceDetailsDTO>> getAttendance(@RequestParam Long startDate,
                                                                    @RequestParam Long endDate,
                                                                    @RequestParam Integer userId) {
        LOGGER.info("In AttendanceController getAttendance");
        CommonResponse<List<AttendanceDetailsDTO>> commonResponse = attendanceService.findAttendanceForUser(userId, startDate, endDate);
        LOGGER.info("In AttendanceController getAttendance");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'EMPLOYEE', 'ADMIN')")
    @GetMapping(
            value = "/image/{attendanceId}",
            produces = "application/octet-stream"
    )
    public ResponseEntity<byte[]> getAttendancePicById(@PathVariable(name = "attendanceId") Integer attendanceId) {
        LOGGER.info("In AttendanceController getAttendancePicById");
        ResponseEntity<byte[]> responseEntity = attendanceService.getAttendanceFileData(attendanceId);
        LOGGER.info("In AttendanceController getAttendancePicById");
        return responseEntity;
    }
}

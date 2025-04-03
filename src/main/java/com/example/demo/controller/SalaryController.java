package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.GenerateSalaryRequestDTO;
import com.example.demo.service.serviceImpl.SalaryService;

@RestController
@RequestMapping("/v1/salary")
public class SalaryController {
    private final Logger LOGGER = LoggerFactory.getLogger(SalaryController.class);
    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @PostMapping
    public CommonResponse<String> generateSalary(@RequestBody GenerateSalaryRequestDTO generateSalaryRequestDTO) {
        LOGGER.info("In AttendanceController getAttendance");
        CommonResponse<String> commonResponse = salaryService.generateSalary(generateSalaryRequestDTO);
        LOGGER.info("In AttendanceController getAttendance");
        return commonResponse;
    }
}

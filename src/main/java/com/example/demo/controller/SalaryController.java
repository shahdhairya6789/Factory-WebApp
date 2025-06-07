package com.example.demo.controller;

import java.util.List;

import com.example.demo.models.dto.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.PaymentListingDTO;
import com.example.demo.models.dto.SalaryRequestDTO;
import com.example.demo.service.serviceImpl.SalaryService;

import java.util.List;

@RestController
@RequestMapping("/v1/salary")
public class SalaryController {
    private final Logger LOGGER = LoggerFactory.getLogger(SalaryController.class);
    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MERCHANT')")
    public CommonResponse<String> generateSalary(@RequestBody SalaryRequestDTO generateSalaryRequestDTO) {
        LOGGER.info("In SalaryController generateSalary");
        CommonResponse<String> commonResponse = salaryService.generateSalary(generateSalaryRequestDTO);
        LOGGER.info("In SalaryController generateSalary");
        return commonResponse;
    }

    @PostMapping("/list")
    public CommonResponse<List<PaymentListingDTO>> getPaymentListingByUserId(@RequestBody SalaryRequestDTO salaryRequestDTO) {
        LOGGER.info("In SalaryController getPaymentListingByUserId");
        CommonResponse<List<PaymentListingDTO>> commonResponse = salaryService.getPaymentListingDTOListing(salaryRequestDTO);
        LOGGER.info("Out SalaryController getPaymentListingByUserId");
        return commonResponse;
    }

    @PostMapping("/regenerate")
    public CommonResponse<String> regenerateSalary(@RequestBody SalaryRequestDTO generateSalaryRequestDTO) {
        LOGGER.info("In SalaryController regenerateSalary");
        CommonResponse<String> commonResponse = salaryService.regenerateSalary(generateSalaryRequestDTO);
        LOGGER.info("In SalaryController regenerateSalary");
        return commonResponse;
    }

//    @PutMapping
//    @PreAuthorize("hasAnyAuthority('MERCHANT')")
//    public CommonResponse<String> reGenerateSalary(@RequestBody GenerateSalaryRequestDTO generateSalaryRequestDTO) {
//        LOGGER.info("In AttendanceController getAttendance");
//        CommonResponse<String> commonResponse = salaryService.generateSalary(generateSalaryRequestDTO);
//        LOGGER.info("In AttendanceController getAttendance");
//        return commonResponse;
//    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MERCHANT', 'EMPLOYEE')")
    public CommonResponse<List<PaymentDTO>> getSalary(@RequestParam(required = false) Integer userId,
                                                  @RequestParam(required = false) Integer managerId,
                                                  @RequestParam Integer month,
                                                  @RequestParam Integer year) {
        LOGGER.info("In SalaryController getSalary");
        if (userId == null && managerId == null) {
            throw new IllegalArgumentException("Either userId or managerId must be provided");
        }
        CommonResponse<List<PaymentDTO>> commonResponse = salaryService.getSalary(userId, managerId, month, year);
        LOGGER.info("Out SalaryController getSalary");
        return commonResponse;
    }
}

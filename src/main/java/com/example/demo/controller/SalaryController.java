package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.PaymentListingDTO;
import com.example.demo.models.dto.SalaryRequestDTO;
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
}

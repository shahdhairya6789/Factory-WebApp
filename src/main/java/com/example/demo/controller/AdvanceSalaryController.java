package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.AdvanceSalaryDTO;
import com.example.demo.service.serviceImpl.AdvanceSalaryService;

@RestController
@RequestMapping("/v1/advance-salary")
public class AdvanceSalaryController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final AdvanceSalaryService advanceSalaryService;

    public AdvanceSalaryController(AdvanceSalaryService advanceSalaryService) {
        this.advanceSalaryService = advanceSalaryService;
    }

    @PreAuthorize("hasAnyAuthority('Merchant')")
    @PostMapping
    public CommonResponse<AdvanceSalaryDTO> addAdvanceSalary(@RequestBody AdvanceSalaryDTO advanceSalaryDTO) {
        LOGGER.info("In AdvanceSalaryController addAdvanceSalary");
        CommonResponse<AdvanceSalaryDTO> commonResponse = advanceSalaryService.saveAdvanceSalary(advanceSalaryDTO);
        LOGGER.info("Out AdvanceSalaryController addAdvanceSalary");
        return commonResponse;
    }

    @PreAuthorize("hasAnyAuthority('Merchant', 'Employee')")
    @GetMapping
    public CommonResponse<List<AdvanceSalaryDTO>> getAdvanceSalary(@RequestParam Integer userId,
                                                                   @RequestParam Long startDate,
                                                                   @RequestParam Long endDate) {
        LOGGER.info("In AdvanceSalaryController getAdvanceSalary");
        CommonResponse<List<AdvanceSalaryDTO>> commonResponse = advanceSalaryService.getAdvanceSalaryByUserId(userId, startDate, endDate);
        LOGGER.info("Out AdvanceSalaryController getAdvanceSalary");
        return commonResponse;
    }
}

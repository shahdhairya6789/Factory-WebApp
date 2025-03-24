package com.example.demo.service.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ApplicationConstants;
import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.AdvanceSalaryDTO;
import com.example.demo.models.entity.mapping.AdvanceSalary;
import com.example.demo.models.entity.master.User;
import com.example.demo.repository.mapping.AdvanceSalaryRepository;
import com.example.demo.repository.master.UserRepository;

@Service
public class AdvanceSalaryService {
    private final Logger LOGGER = LoggerFactory.getLogger(AdvanceSalaryService.class);
    private final AdvanceSalaryRepository advanceSalaryRepository;
    private final UserRepository userRepository;

    public AdvanceSalaryService(AdvanceSalaryRepository advanceSalaryRepository, UserRepository userRepository) {
        this.advanceSalaryRepository = advanceSalaryRepository;
        this.userRepository = userRepository;
    }

    public CommonResponse<AdvanceSalaryDTO> saveAdvanceSalary(AdvanceSalaryDTO advanceSalaryDTO) {
        LOGGER.info("In AdvanceSalaryService.saveAdvanceSalary");
        User user = userRepository.findById(advanceSalaryDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_USER_ID_MESSAGE));
        User paidByUser = userRepository.findById(advanceSalaryDTO.getPaidByUserId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_USER_ID_MESSAGE));
        AdvanceSalary advanceSalary = new AdvanceSalary();
        advanceSalary.setUser(user);
        advanceSalary.setPaidByUser(paidByUser);
        advanceSalary.setAdvanceSalary(advanceSalaryDTO.getAdvanceSalaryAmount());
        advanceSalary.setAdvanceSalaryDate(new Timestamp(System.currentTimeMillis()));
        advanceSalary = advanceSalaryRepository.save(advanceSalary);
        advanceSalaryDTO = new AdvanceSalaryDTO(advanceSalary);
        LOGGER.info("Out AdvanceSalaryService.saveAdvanceSalary");
        return new CommonResponse<>(advanceSalaryDTO, "AdvanceSalary saved successfully");
    }

    public CommonResponse<List<AdvanceSalaryDTO>> getAdvanceSalaryByUserId(int userId, long startTime, long endTime) {
        LOGGER.info("In AdvanceSalaryService getAdvanceSalaryByUserId");
        Timestamp startDateFilter = new Timestamp(startTime * 1000L);
        Timestamp endDateFilter = new Timestamp(endTime * 1000L);
        List<AdvanceSalaryDTO> advanceSalaryDTOList = advanceSalaryRepository.fetchUserAdvanceSalary(userId, startDateFilter, endDateFilter);
        LOGGER.info("Out AdvanceSalaryService getAdvanceSalaryByUserId");
        return new CommonResponse<>(advanceSalaryDTOList, "User Advance Salary List fetched successfully");
    }
}

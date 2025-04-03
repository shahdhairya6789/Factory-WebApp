package com.example.demo.service.serviceImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.GenerateSalaryRequestDTO;
import com.example.demo.models.dto.UserAdvanceSalaryDTO;
import com.example.demo.models.dto.UserSalaryAttendanceDTO;
import com.example.demo.models.entity.constant.SalaryType;
import com.example.demo.models.entity.mapping.Payment;
import com.example.demo.models.entity.master.User;
import com.example.demo.repository.constant.SalaryTypeRepository;
import com.example.demo.repository.mapping.AdvanceSalaryRepository;
import com.example.demo.repository.mapping.PaymentRepository;
import com.example.demo.repository.master.AttendanceRepository;
import com.example.demo.repository.master.UserRepository;

@Service
public class SalaryService {
    private final Logger LOGGER = LoggerFactory.getLogger(SalaryService.class);
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final SalaryTypeRepository salaryTypeRepository;
    private final AdvanceSalaryRepository advanceSalaryRepository;
    private final PaymentRepository paymentRepository;

    public SalaryService(UserRepository userRepository,
                         AttendanceRepository attendanceRepository,
                         SalaryTypeRepository salaryTypeRepository,
                         AdvanceSalaryRepository advanceSalaryRepository,
                         PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.salaryTypeRepository = salaryTypeRepository;
        this.advanceSalaryRepository = advanceSalaryRepository;
        this.paymentRepository = paymentRepository;
    }

    public CommonResponse<String> generateSalary(GenerateSalaryRequestDTO generateSalaryRequestDTO) {
        LOGGER.debug("In SalaryService generateSalary");
        String response = "No attendance present so no salary generated";
        if (Objects.isNull(generateSalaryRequestDTO.getMonth()) || Objects.isNull(generateSalaryRequestDTO.getYear())) {
            throw new IllegalArgumentException("Start time and End Time need to be set");
        }
        if (Objects.isNull(generateSalaryRequestDTO.getUserId()) && Objects.isNull(generateSalaryRequestDTO.getManagerId())) {
            throw new IllegalArgumentException("Provide valid userId and managerId");
        }
        Map<Integer, User> userIdToUser = new HashMap<>();
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth(), 1, 0, 0));
        YearMonth yearMonth = YearMonth.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth());
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        Timestamp endDate = Timestamp.valueOf(endOfMonth);
        Timestamp currentDate = Timestamp.from(Instant.now());
        if (Objects.nonNull(generateSalaryRequestDTO.getUserId())) {
            userIdToUser = userRepository.findById(generateSalaryRequestDTO.getUserId())
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    User::getId, Function.identity()
                            )
                    );
        }
        if (Objects.nonNull(generateSalaryRequestDTO.getManagerId())) {
            userIdToUser = userRepository.findUsersByManager_Id(generateSalaryRequestDTO.getManagerId())
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    User::getId, Function.identity()
                            )
                    );
        }

        Map<Integer, SalaryType> salaryTypeIdToSalaryTypeObject = salaryTypeRepository.findAll().stream().collect(Collectors.toMap(SalaryType::getId, Function.identity()));
        List<UserSalaryAttendanceDTO> userSalaryAttendanceDTOList = attendanceRepository.findUserSalaryAttendanceByUserId(userIdToUser.keySet(), startDate, endDate);
        Map<Integer, Double> userIdToAdvancePaymentMap = advanceSalaryRepository.fetchAdvanceSalaryForUsersBetweenStartAndEndDate(userIdToUser.keySet(), startDate, endDate).stream().collect(Collectors.toMap(UserAdvanceSalaryDTO::getUserId, UserAdvanceSalaryDTO::getAdvanceSalaryAmount));
        List<Payment> paymentList = new ArrayList<>();
        for (UserSalaryAttendanceDTO userSalaryAttendanceDTO : userSalaryAttendanceDTOList) {
            Payment payment = new Payment();
            payment.setSalaryType(salaryTypeIdToSalaryTypeObject.get(userSalaryAttendanceDTO.getSalaryTypeId()));
            payment.setPaymentDate(currentDate);
            payment.setUser(userIdToUser.get(userSalaryAttendanceDTO.getUserId()));
            payment.setPaymentAmount((int) userSalaryAttendanceDTO.getPayment());
            if (userSalaryAttendanceDTO.getSalaryTypeId() == SalaryType.SalaryTypeValues.ONE_MACHINE.getId()) {
                int advanceSalary = userIdToAdvancePaymentMap.getOrDefault(userSalaryAttendanceDTO.getUserId(), 0.0).intValue();
                payment.setAdvancePayment(advanceSalary);
            }
            paymentList.add(payment);
        }
        if (CollectionUtils.isNotEmpty(paymentList)) {
            paymentRepository.saveAll(paymentList);
            response = "Salary Generation Successfully";
        }
        LOGGER.debug("Out SalaryService generateSalary");
        return new CommonResponse<>(response);
    }
}

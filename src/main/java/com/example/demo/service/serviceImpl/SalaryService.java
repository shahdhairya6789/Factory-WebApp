package com.example.demo.service.serviceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.demo.models.dto.PaymentDTO;
import com.example.demo.models.entity.mapping.Salary;
import com.example.demo.models.entity.master.CustomUserDetails;
import com.example.demo.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.SalaryRequestDTO;
import com.example.demo.models.dto.PaymentListingDTO;
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

    public CommonResponse<String> generateSalary(SalaryRequestDTO generateSalaryRequestDTO) {
        LOGGER.debug("In SalaryService generateSalary");
        String response = "No attendance present so no salary generated";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUserId();
        validateRequest(generateSalaryRequestDTO);
        Map<Integer, User> userIdToUser = getIntegerUserMap(generateSalaryRequestDTO);
        DateUtils.DateRecord result = DateUtils.getDateRecord(generateSalaryRequestDTO);
        Map<Integer, SalaryType> salaryTypeIdToSalaryTypeObject = salaryTypeRepository.findAll().stream().collect(Collectors.toMap(SalaryType::getId, Function.identity()));
        List<UserSalaryAttendanceDTO> userSalaryAttendanceDTOList = attendanceRepository.findUserSalaryAttendanceByUserId(userIdToUser.keySet(), result.startDate(), result.endDate());
        Map<Long, BigDecimal> userIdToAdvancePaymentMap = advanceSalaryRepository.fetchAdvanceSalaryForUsersBetweenStartAndEndDate(userIdToUser.keySet(), result.startDate(), result.endDate()).stream().collect(Collectors.toMap(UserAdvanceSalaryDTO::getUserId, UserAdvanceSalaryDTO::getAdvanceSalaryAmount));
        List<Payment> paymentList = new ArrayList<>();
        for (UserSalaryAttendanceDTO userSalaryAttendanceDTO : userSalaryAttendanceDTOList) {
            int paymentAmount = (int) (userSalaryAttendanceDTO.getMonthlySalary() * userSalaryAttendanceDTO.getWorkingDays());
            Payment payment = new Payment();
            payment.setSalaryType(salaryTypeIdToSalaryTypeObject.get((int) userSalaryAttendanceDTO.getSalaryTypeId()));
            payment.setPaymentDate(result.endDate());
            payment.setWorkingDays((int) userSalaryAttendanceDTO.getWorkingDays());
            payment.setUser(userIdToUser.get((int) userSalaryAttendanceDTO.getUserId()));
            payment.setPaymentAmount(paymentAmount);
            if (userSalaryAttendanceDTO.getSalaryTypeId() == SalaryType.SalaryTypeValues.ONE_MACHINE.getId()) {
                int advanceSalary = userIdToAdvancePaymentMap.getOrDefault(userSalaryAttendanceDTO.getUserId(), BigDecimal.ZERO).intValue();
                payment.setAdvancePayment(advanceSalary);
            }
            payment.setCreatedBy(currentUserId);
            payment.setModifiedBy(currentUserId);
            payment.setActive(true);
            paymentList.add(payment);
        }
        if (CollectionUtils.isNotEmpty(paymentList)) {
            paymentRepository.saveAll(paymentList);
            response = "Salary Generation Successfully";
        }
        LOGGER.debug("Out SalaryService generateSalary");
        return new CommonResponse<>(response);
    }

    private static void validateRequest(SalaryRequestDTO generateSalaryRequestDTO) {
        if (Objects.isNull(generateSalaryRequestDTO.getMonth()) || Objects.isNull(generateSalaryRequestDTO.getYear())) {
            throw new IllegalArgumentException("Start time and End Time need to be set");
        }
        if (Objects.isNull(generateSalaryRequestDTO.getUserId()) && Objects.isNull(generateSalaryRequestDTO.getManagerId())) {
            throw new IllegalArgumentException("Provide valid userId and managerId");
        }
    }

    private Map<Integer, User> getIntegerUserMap(SalaryRequestDTO generateSalaryRequestDTO) {
        Map<Integer, User> userIdToUser = new HashMap<>();
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
        return userIdToUser;
    }

//    private static DateRecord getDateRecord(SalaryRequestDTO generateSalaryRequestDTO) {
//        Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth(), 1, 0, 0));
//        YearMonth yearMonth = YearMonth.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth());
//        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
//        Timestamp endDate = Timestamp.valueOf(endOfMonth);
//        Timestamp currentDate = Timestamp.from(Instant.now());
//        return new DateRecord(startDate, endDate, currentDate);
//    }
//
//    private record DateRecord(Timestamp startDate, Timestamp endDate, Timestamp currentDate) {
//    }

    public CommonResponse<List<PaymentListingDTO>> getPaymentListingDTOListing(SalaryRequestDTO salaryRequestDTO) {
        LOGGER.debug("In SalaryService getPaymentListingDTOListing");
        validateRequest(salaryRequestDTO);
        DateUtils.DateRecord result = DateUtils.getDateRecord(salaryRequestDTO);
        List<PaymentListingDTO> paymentList = paymentRepository.getPaymentListingByUserId(
                Objects.nonNull(salaryRequestDTO.getUserId()) ? salaryRequestDTO.getUserId() : 0,
                Objects.nonNull(salaryRequestDTO.getManagerId()) ? salaryRequestDTO.getManagerId() : 0,
                result.startDate(),
                result.endDate()
        );
        LOGGER.debug("In SalaryService getPaymentListingDTOListing");
            return new CommonResponse<>(paymentList, "Salary Listed Successfully");
    }

    public CommonResponse<String> regenerateSalary(SalaryRequestDTO salaryRequestDTO) {
        LOGGER.debug("In SalaryService regenerateSalary");
        validateRequest(salaryRequestDTO);
        Map<Integer, User> userIdToUser = getIntegerUserMap(salaryRequestDTO);
        DateUtils.DateRecord result = DateUtils.getDateRecord(salaryRequestDTO);
        List<Payment> payments = paymentRepository.findByUserIdInAndPaymentDateBetween(userIdToUser.keySet(), result.startDate(), result.endDate());
        CommonResponse<String> commonResponse = generateSalary(salaryRequestDTO);
        payments.forEach(payment -> payment.setActive(false));
        paymentRepository.saveAll(payments);
        LOGGER.debug("Out SalaryService regenerateSalary");
        return commonResponse;
    }

//    public CommonResponse<String> reGenerateSalary(GenerateSalaryRequestDTO generateSalaryRequestDTO) {
//        LOGGER.debug("In SalaryService reGenerateSalary");
//        String response = "No attendance present so no salary generated";
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Integer currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUserId();
//        if (Objects.isNull(generateSalaryRequestDTO.getMonth()) || Objects.isNull(generateSalaryRequestDTO.getYear())) {
//            throw new IllegalArgumentException("Start time and End Time need to be set");
//        }
//        if (Objects.isNull(generateSalaryRequestDTO.getUserId()) && Objects.isNull(generateSalaryRequestDTO.getManagerId())) {
//            throw new IllegalArgumentException("Provide valid userId and managerId");
//        }
//        Map<Integer, User> userIdToUser = new HashMap<>();
//        Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth(), 1, 0, 0));
//        YearMonth yearMonth = YearMonth.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth());
//        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
//        Timestamp endDate = Timestamp.valueOf(endOfMonth);
//        Timestamp currentDate = Timestamp.from(Instant.now());
//        if (Objects.nonNull(generateSalaryRequestDTO.getUserId())) {
//            userIdToUser = userRepository.findById(generateSalaryRequestDTO.getUserId())
//                    .stream()
//                    .collect(
//                            Collectors.toMap(
//                                    User::getId, Function.identity()
//                            )
//                    );
//        }
//        if (Objects.nonNull(generateSalaryRequestDTO.getManagerId())) {
//            userIdToUser = userRepository.findUsersByManager_Id(generateSalaryRequestDTO.getManagerId())
//                    .stream()
//                    .collect(
//                            Collectors.toMap(
//                                    User::getId, Function.identity()
//                            )
//                    );
//        }
//
////        Map<Integer, SalaryType> salaryTypeIdToSalaryTypeObject = salaryTypeRepository.findAll().stream().collect(Collectors.toMap(SalaryType::getId, Function.identity()));
//        List<UserSalaryAttendanceDTO> userSalaryAttendanceDTOList = attendanceRepository.findUserSalaryAttendanceByUserId(userIdToUser.keySet(), startDate, endDate);
//        Map<Long, BigDecimal> userIdToAdvancePaymentMap = advanceSalaryRepository.fetchAdvanceSalaryForUsersBetweenStartAndEndDate(userIdToUser.keySet(), startDate, endDate).stream().collect(Collectors.toMap(UserAdvanceSalaryDTO::getUserId, UserAdvanceSalaryDTO::getAdvanceSalaryAmount));
//        List<Payment> paymentList = new ArrayList<>();
//        for (UserSalaryAttendanceDTO userSalaryAttendanceDTO : userSalaryAttendanceDTOList) {
//            double paymentAmount = (double) (userSalaryAttendanceDTO.getMonthlySalary() * userSalaryAttendanceDTO.getWorkingDays() / 365);
//            Payment payment = new Payment();
////            payment.setSalaryType(salaryTypeIdToSalaryTypeObject.get((int) userSalaryAttendanceDTO.getSalaryTypeId()));
//            payment.setPaymentDate(currentDate);
//            payment.setWorkingDays((int) userSalaryAttendanceDTO.getWorkingDays());
//            payment.setUser(userIdToUser.get((int) userSalaryAttendanceDTO.getUserId()));
//            payment.setPaymentAmount(paymentAmount);
//            if (userSalaryAttendanceDTO.getSalaryTypeId() == SalaryType.SalaryTypeValues.ONE_MACHINE.getId()) {
//                int advanceSalary = userIdToAdvancePaymentMap.getOrDefault(userSalaryAttendanceDTO.getUserId(), BigDecimal.ZERO).intValue();
//                payment.setAdvancePayment(advanceSalary);
//            }
//            payment.setCreatedBy(currentUserId);
//            payment.setModifiedBy(currentUserId);
//            paymentList.add(payment);
//        }
//        if (CollectionUtils.isNotEmpty(paymentList)) {
//            paymentRepository.saveAll(paymentList);
//            response = "Salary Generation Successfully";
//        }
//        LOGGER.debug("Out SalaryService generateSalary");
//        return new CommonResponse<>(response);
//    }

    public CommonResponse<List<PaymentDTO>> getSalary(Integer userId, Integer managerId, Integer month, Integer year) {
        LOGGER.debug("In SalaryService getSalary");
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(year, month, 1, 0, 0));
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        Timestamp endDate = Timestamp.valueOf(endOfMonth);

        List<PaymentDTO> salaries;
        if (userId != null) {
            salaries = paymentRepository.findPaymentsByUserIdAndPaymentDateBetween(userId, startDate, endDate);
        } else {
            salaries = paymentRepository.findPaymentsByManagerIdAndPaymentDateBetween(managerId, startDate, endDate);
        }

        LOGGER.debug("Out SalaryService getSalary");
        return new CommonResponse<>(salaries, "Salaries fetched successfully");
    }
}

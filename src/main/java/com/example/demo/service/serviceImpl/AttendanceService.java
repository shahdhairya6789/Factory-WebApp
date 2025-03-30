package com.example.demo.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constants.ApplicationConstants;
import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.AttendanceDetailsDTO;
import com.example.demo.models.dto.AttendanceVO;
import com.example.demo.models.entity.constant.SalaryType;
import com.example.demo.models.entity.constant.Shift;
import com.example.demo.models.entity.master.Attendance;
import com.example.demo.models.entity.master.Machine;
import com.example.demo.models.entity.master.User;
import com.example.demo.repository.constant.SalaryTypeRepository;
import com.example.demo.repository.constant.ShiftRepository;
import com.example.demo.repository.master.AttendanceRepository;
import com.example.demo.repository.master.MachineRepository;
import com.example.demo.repository.master.UserRepository;

@Service
public class AttendanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceService.class);
    private final UserRepository userRepository;
    private final MachineRepository machineRepository;
    private final ShiftRepository shiftRepository;
    private final SalaryTypeRepository salaryTypeRepository;
    private final AttendanceRepository attendanceRepository;
    private final String attendanceFilePath;
    private final long attendanceMaxFileSize;

    public AttendanceService(UserRepository userRepository,
                             MachineRepository machineRepository,
                             ShiftRepository shiftRepository,
                             SalaryTypeRepository salaryTypeRepository,
                             AttendanceRepository attendanceRepository,
                             Environment environment) {
        this.userRepository = userRepository;
        this.machineRepository = machineRepository;
        this.shiftRepository = shiftRepository;
        this.salaryTypeRepository = salaryTypeRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceFilePath = environment.getProperty("attendance.file.path");
        this.attendanceMaxFileSize = Long.parseLong(Objects.requireNonNull(environment.getProperty("attendance.max.file.size")));
    }

    public CommonResponse<Attendance> addAttendance(MultipartFile file, AttendanceVO attendanceVO) throws Exception {
        LOGGER.info("In AttendanceService addAttendance");
        long startDateEpochMilli = Instant.ofEpochMilli(attendanceVO.getAttendanceDate() * 1000L).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endDateEpochMilli = Instant.ofEpochMilli(attendanceVO.getAttendanceDate() * 1000L).atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // Validate request
        User user = userRepository.findById(attendanceVO.getUserId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_USER_ID_MESSAGE));
        Machine machine = machineRepository.findById(attendanceVO.getMachineId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_MACHINE_ID_MESSAGE));
        Shift shift = shiftRepository.findById(attendanceVO.getShiftId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_SHIFT_ID_MESSAGE));
        List<AttendanceDetailsDTO> attendanceList = attendanceRepository.fetchAttendanceDetails(user.getId(), new Timestamp(startDateEpochMilli), new Timestamp(endDateEpochMilli));
        SalaryType salaryType = salaryTypeRepository.findById(SalaryType.SalaryTypeValues.ONE_MACHINE.getId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_SALARY_TYPE_ID_MESSAGE));
        if (CollectionUtils.isNotEmpty(attendanceList)) {
            salaryType = salaryTypeRepository.findById(SalaryType.SalaryTypeValues.TWO_MACHINE.getId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_SALARY_TYPE_ID_MESSAGE));
        }
        if (Objects.isNull(file) || file.isEmpty() || file.getSize() >= attendanceMaxFileSize) {
            throw new IllegalArgumentException("File is null or empty");
        }
        String attendanceDate = new SimpleDateFormat("yyyy/MM/dd").format((long) attendanceVO.getAttendanceDate() * 1000);
        String managerName = Objects.isNull(user.getManager()) ? "" : user.getManager().getName();
        String filePath = String.format(attendanceFilePath, attendanceDate, managerName, user.getName());
        try {
            saveImage(file, filePath);
        } catch (IOException e) {
            LOGGER.error("Exception while saving the attendance image {}", ExceptionUtils.getStackTrace(e), e);
            throw new Exception("Exception while saving the attendance image");
        }

        // Add attendance
        Attendance attendance = new Attendance(attendanceVO, user, machine, shift, salaryType, attendanceDate, file.getSize(), filePath);
        attendance = attendanceRepository.save(attendance);
        LOGGER.info("Out AttendanceService addAttendance");
        return new CommonResponse<>(attendance, "Success");
    }

    public void saveImage(MultipartFile file, String storagePath) throws IOException {
        // Ensure the folder exists
        File directory = new File(storagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Save the image
        File destination = new File(storagePath + File.separator + file.getOriginalFilename());
        file.transferTo(destination);
    }

    public CommonResponse<List<AttendanceDetailsDTO>> findAttendanceForUser(int userId, Long startDate, Long endDate) {
        LOGGER.info("In AttendanceService findAttendanceForUser");
        Timestamp startDateFilter = new Timestamp(startDate * 1000L);
        Timestamp endDateFilter = new Timestamp(endDate * 1000L);
        List<AttendanceDetailsDTO> attendanceList = attendanceRepository.fetchAttendanceDetails(userId, startDateFilter, endDateFilter);
        LOGGER.info("Out AttendanceService findAttendanceForUser");
        return new CommonResponse<>(attendanceList, "Attendance List fetched successfully");
    }
}

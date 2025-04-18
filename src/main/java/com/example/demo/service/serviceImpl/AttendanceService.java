package com.example.demo.service.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import com.example.demo.models.entity.master.CustomUserDetails;
import com.example.demo.util.DateUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String filePath = getAttendanceFilePath(attendanceVO.getAttendanceDate(), user);
        String attendanceDate = new SimpleDateFormat("yyyy/MM/dd").format(attendanceVO.getAttendanceDate() * 1000);
        try {
            saveImage(file, filePath);
        } catch (IOException e) {
            LOGGER.error("Exception while saving the attendance image {}", ExceptionUtils.getStackTrace(e), e);
            throw new Exception("Exception while saving the attendance image");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUserId();
        // Add attendance
        Attendance attendance = new Attendance(attendanceVO, user, machine, shift, salaryType, attendanceDate, file.getSize(), filePath, currentUserId, currentUserId);
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
        Timestamp startDateFilter = DateUtils.getStartOfDayTimestamp(startDate);
        Timestamp endDateFilter = DateUtils.getEndOfDayTimestamp(endDate);
        List<AttendanceDetailsDTO> attendanceList = attendanceRepository.fetchAttendanceDetails(userId, startDateFilter, endDateFilter);
        LOGGER.info("Out AttendanceService findAttendanceForUser");
        return new CommonResponse<>(attendanceList, "Attendance List fetched successfully");
    }

    public ResponseEntity<byte[]> getAttendanceFileData(int attendanceId) {
        LOGGER.info("In AttendanceService getAttendanceFileData");
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance id " + attendanceId));
        String filePath = getAttendanceFilePath(attendance.getAttendanceDate().getTime(), attendance.getUser());
        String fileName = attendance.getAttendanceUserImageName();
        File file = new File(filePath + File.separator + fileName);
        byte[] zippedData;
        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }
        try {
            zippedData = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading file");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(zippedData.length);
        LOGGER.info("Out AttendanceService getAttendanceFileData");
        return ResponseEntity.ok().headers(httpHeaders).body(zippedData);
    }

    private String getAttendanceFilePath(long attendanceEpoch,
                                         User user) {
        String attendanceDate = new SimpleDateFormat("yyyy/MM/dd").format(attendanceEpoch * 1000);
        String managerName = Objects.isNull(user.getManager()) ? "Default" : user.getManager().getName();
        return String.format(attendanceFilePath, managerName, user.getName(), attendanceDate);
    }
}

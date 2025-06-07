package com.example.demo.util;

import com.example.demo.models.dto.SalaryRequestDTO;

import java.sql.Timestamp;
import java.time.*;

public class DateUtils {

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    public static Timestamp getEndOfDayTimestamp(long epochSeconds) {
        // Convert epochSeconds to Instant (UTC)
        Instant instant = Instant.ofEpochSecond(epochSeconds);

        // Convert to IST (Asia/Kolkata)
        ZonedDateTime istDateTime = instant.atZone(ZoneId.of("Asia/Kolkata"));

        // Get end of the day in IST
        LocalDate endOfDayIST = istDateTime.toLocalDate();
        LocalDateTime endOfDay = endOfDayIST.atTime(23, 59, 59, 999999999);

        // Convert to Timestamp
        return Timestamp.valueOf(endOfDay);
    }

    public static Timestamp getStartOfDayTimestamp(long epochSeconds) {
        // Convert epochSeconds to Instant
        Instant instant = Instant.ofEpochSecond(epochSeconds);

        // Convert to IST Zone
        ZonedDateTime istDateTime = instant.atZone(ZoneId.of("Asia/Kolkata"));

        // Get start of the day in IST
        LocalDate startOfDay = istDateTime.toLocalDate();
        LocalDateTime startOfDayIST = startOfDay.atStartOfDay();

        // Convert to Timestamp
        return Timestamp.valueOf(startOfDayIST);
    }

    public static DateRecord getDateRecord(SalaryRequestDTO generateSalaryRequestDTO) {
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth(), 1, 0, 0));
        YearMonth yearMonth = YearMonth.of(generateSalaryRequestDTO.getYear(), generateSalaryRequestDTO.getMonth());
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        Timestamp endDate = Timestamp.valueOf(endOfMonth);
        Timestamp currentDate = Timestamp.from(Instant.now());
        return new DateRecord(startDate, endDate, currentDate);
    }

    public static record DateRecord(Timestamp startDate, Timestamp endDate, Timestamp currentDate) {
    }
}

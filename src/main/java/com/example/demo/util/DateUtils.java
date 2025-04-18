package com.example.demo.util;

import java.sql.Timestamp;
import java.time.*;

public class DateUtils {
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
}

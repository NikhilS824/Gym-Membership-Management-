package com.gymmanagement.util;

import com.gymmanagement.enums.DurationUnit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static LocalDate calculateEndDate(LocalDate startDate, Integer durationValue, DurationUnit durationUnit) {
        if (startDate == null || durationValue == null || durationValue <= 0 || durationUnit == null) {
            return startDate;
        }

        return switch (durationUnit) {
            case MONTH -> startDate.plusMonths(durationValue).minusDays(1);
            case QUARTER -> startDate.plusMonths(durationValue * 3L).minusDays(1);
            case HALF_YEAR -> startDate.plusMonths(durationValue * 6L).minusDays(1);
            case YEAR -> startDate.plusYears(durationValue).minusDays(1);
            case CUSTOM -> startDate.plusDays(durationValue).minusDays(1);
        };
    }

    public static long calculateDaysRemaining(LocalDate endDate) {
        if (endDate == null) return 0;
        LocalDate today = LocalDate.now();
        if (today.isAfter(endDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(today, endDate);
    }
}

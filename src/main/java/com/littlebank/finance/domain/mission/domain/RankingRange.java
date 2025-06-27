package com.littlebank.finance.domain.mission.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum RankingRange {
    WEEK(1, ChronoUnit.WEEKS),
    TWO_WEEKS(2, ChronoUnit.WEEKS),
    ONE_MONTH(1, ChronoUnit.MONTHS),
    TWO_MONTHS(2, ChronoUnit.MONTHS);

    private final int amount;
    private final ChronoUnit unit;

    RankingRange(int amount, ChronoUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }
    public LocalDateTime calculateStartDay(LocalDate today) {
        if (unit == ChronoUnit.MONTHS) {
            return today.withDayOfMonth(1).atStartOfDay();
        } else {
            return today.with(DayOfWeek.MONDAY).atStartOfDay();
        }
    }

    public LocalDateTime calculateEndDay(LocalDate today) {
        if (unit == ChronoUnit.MONTHS) {
            LocalDate targetMonth = today.plusMonths(amount - 1);
            LocalDate endOfMonth = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth());
            return endOfMonth.atTime(23, 59, 59);
        } else {
            LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
            LocalDate endOfPeriod = startOfWeek.plusWeeks(amount).with(DayOfWeek.SUNDAY);
            return endOfPeriod.atTime(23, 59, 59);
        }
    }
}

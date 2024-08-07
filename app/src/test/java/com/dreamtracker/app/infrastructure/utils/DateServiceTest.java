package com.dreamtracker.app.infrastructure.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;



class DateServiceTest {

    DateService dateService = new DateService();
    Instant fixedDate = ZonedDateTime.of(2023, 9, 1, 0, 0, 0, 0, ZoneId.systemDefault()).toInstant();

    @Test
    void getCooldownPeriodBasedOnCurrentDateDaily() {
        Instant dailyCooldown = dateService.getCooldownPeriodBasedOnCurrentDate(fixedDate, "DAILY");
        LocalDateTime expectedDaily = LocalDateTime.ofInstant(fixedDate, ZoneId.systemDefault()).plusDays(1).toLocalDate().atStartOfDay();
        assertEquals(expectedDaily.atZone(ZoneId.systemDefault()).toInstant(), dailyCooldown);
    }

    @Test
    void getCooldownPeriodBasedOnCurrentDateWeekly() {
        Instant weeklyCooldown = dateService.getCooldownPeriodBasedOnCurrentDate(fixedDate, "WEEKLY");
        LocalDateTime expectedWeekly = LocalDateTime.ofInstant(fixedDate, ZoneId.systemDefault()).with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        assertEquals(expectedWeekly.atZone(ZoneId.systemDefault()).toInstant(), weeklyCooldown);
    }

    @Test
    void getCooldownPeriodBasedOnCurrentDateMonthly() {
        Instant monthlyCooldown = dateService.getCooldownPeriodBasedOnCurrentDate(fixedDate, "MONTHLY");
        LocalDateTime expectedMonthly = LocalDateTime.ofInstant(fixedDate, ZoneId.systemDefault()).with(java.time.temporal.TemporalAdjusters.firstDayOfNextMonth()).toLocalDate().atStartOfDay();
        assertEquals(expectedMonthly.atZone(ZoneId.systemDefault()).toInstant(), monthlyCooldown);
    }
}

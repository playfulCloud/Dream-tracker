package com.dreamtracker.app.infrastructure.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class DateService {

  public Instant getCurrentDateInISO8601() {
    return Instant.now();
    }

    public String getSpecificDateInISO8601(int year, int month, int day, int hour, int minute, int second, String zoneId) {
        ZonedDateTime specificDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, java.time.ZoneId.of(zoneId));
        return specificDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

  public Instant getCooldownPeriodBasedOnCurrentDate(Instant currentDate, String frequency) {
    Instant result = null;
    LocalDateTime localDateTime = LocalDateTime.ofInstant(currentDate, ZoneId.systemDefault());
    switch (frequency) {
      case "DAILY":
        result =
            localDateTime
                .plusDays(1)
                .toLocalDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        break;
      case "WEEKLY":
        result =
            localDateTime
                .with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.MONDAY))
                .toLocalDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        break;
      case "MONTHLY":
        result = localDateTime.with(java.time.temporal.TemporalAdjusters.firstDayOfNextMonth())
                .toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        break;
    }

    return result;
  }
}

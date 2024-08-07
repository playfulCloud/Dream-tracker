package com.dreamtracker.app.infrastructure.utils;

import java.time.Instant;
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
}

package com.dreamtracker.app.infrastructure.utils;

import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateService {

    public String getCurrentDateInISO8601() {
        ZonedDateTime now = ZonedDateTime.now();
        return now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public String getSpecificDateInISO8601(int year, int month, int day, int hour, int minute, int second, String zoneId) {
        ZonedDateTime specificDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, java.time.ZoneId.of(zoneId));
        return specificDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
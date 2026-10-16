package com.medici.app.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static final String EUROPE_ROME = "Europe/Rome";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSS z");

    public static Date datenow() {
        return Calendar.getInstance(TimeZone.getTimeZone(EUROPE_ROME)).getTime();
    }

    public static OffsetDateTime convert(String updatedAt) {
        try {
            return OffsetDateTime.parse(updatedAt, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
}
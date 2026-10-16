package com.medici.app.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static final String EUROPE_ROME = "Europe/Rome";

    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss:SSS z";
    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String YYYY_MM_DD_T_HH_MM_SS_SSSSSS_Z = "yyyy-MM-dd'T'HH:mm:ss:SSS'Z'";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS_SSS_Z);

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

    public static LocalDate convert2LocalDate(String updatedAt, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(updatedAt, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime convert2LocalDateTime(String updatedAt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
            return LocalDateTime.parse(updatedAt, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static OffsetDateTime convertWithZone(Date updatedAt) {
        try {
            return updatedAt.toInstant().atZone(ZoneId.of(EUROPE_ROME)).toOffsetDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static OffsetDateTime convert(Date updatedAt) {
        try {
            return updatedAt.toInstant().atOffset(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }

    public static OffsetDateTime convert(LocalDateTime localDateTime) {
        try {
            return localDateTime.atOffset(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date convert2Date(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
            return format.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convert(OffsetDateTime odtInstanceAtUTC) {
        try {
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);
            return odtInstanceAtUTC.toLocalDate().format(DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertWithISO_DATE_TIME(OffsetDateTime odtInstanceAtUTC) {
        try {
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
            return odtInstanceAtUTC.toLocalDateTime().format(DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date convert2DateWithformat(String dateInstance, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.parse(dateInstance);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convert2StringWithformat(Date dateInstance, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.format(dateInstance);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convert2String(Date dateInstance) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return format.format(dateInstance);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertStringFromTo(String strDate, String fromFormat, String toFormat) {
        try {
            DateFormat to = new SimpleDateFormat(toFormat); // wanted format
            DateFormat from = new SimpleDateFormat(fromFormat);
            return to.format(from.parse(strDate));
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertDateNowToString(Date date) {
        return convert2StringWithformat(date, YYYY_MM_DD_HH_MM_SS_SSS);
    }

    public static Date convertDateNowStringToDate(String stringDate) {
        return convert2DateWithformat(stringDate, YYYY_MM_DD_HH_MM_SS_SSS);
    }
}
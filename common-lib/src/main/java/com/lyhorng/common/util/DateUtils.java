package com.lyhorng.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)) : null;
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)) : null;
    }
    
    public static String formatDate(LocalDate date, String pattern) {
        return date != null ? date.format(DateTimeFormatter.ofPattern(pattern)) : null;
    }
    
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern(pattern)) : null;
    }
    
    public static LocalDate parseDate(String dateStr) {
        return dateStr != null ? LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)) : null;
    }
    
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)) : null;
    }
    
    public static LocalDate parseDate(String dateStr, String pattern) {
        return dateStr != null ? LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern)) : null;
    }
    
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern)) : null;
    }
    
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
    
    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
    }
    
    public static long daysBetween(LocalDate start, LocalDate end) {
        return start != null && end != null ? ChronoUnit.DAYS.between(start, end) : 0;
    }
    
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return start != null && end != null ? ChronoUnit.HOURS.between(start, end) : 0;
    }
    
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return start != null && end != null ? ChronoUnit.MINUTES.between(start, end) : 0;
    }
    
    public static boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }
    
    public static LocalDate getStartOfMonth(LocalDate date) {
        return date != null ? date.withDayOfMonth(1) : null;
    }
    
    public static LocalDate getEndOfMonth(LocalDate date) {
        return date != null ? date.withDayOfMonth(date.lengthOfMonth()) : null;
    }
    
    public static LocalDateTime getStartOfDay(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }
    
    public static LocalDateTime getEndOfDay(LocalDate date) {
        return date != null ? date.atTime(LocalTime.MAX) : null;
    }
}
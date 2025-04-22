package com.ferremax.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateUtils {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
            .ofPattern("HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    public static String format(LocalDate date) {
        return (date == null) ? "" : date.format(DATE_FORMATTER);
    }

    public static String format(LocalTime time) {
        return (time == null) ? "" : time.format(TIME_FORMATTER);
    }

    public static LocalDate parseDate(String dateString) {
        if (!ValidationUtils.isNotNullOrEmpty(dateString)) {
            return null;
        }
        try {
            return LocalDate.parse(dateString.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear fecha '" + dateString + "': " + e.getMessage());
            return null;
        }
    }

    public static LocalTime parseTime(String timeString) {
        if (!ValidationUtils.isNotNullOrEmpty(timeString)) {
            return null;
        }
        try {
            return LocalTime.parse(timeString.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear hora '" + timeString + "': " + e.getMessage());
            return null;
        }
    }
}

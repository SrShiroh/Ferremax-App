package com.ferremax.util;

import java.util.regex.Pattern;
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[(]?\\d{1,4}[)]?[-\\s./0-9]*\\d{1,}[-\\s./0-9]*\\d{1,}$");
    private static final int MIN_PHONE_DIGITS = 9;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}'\\s]+$");

    public static boolean isNotNullOrEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (!isNotNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (!isNotNullOrEmpty(phone)) {
            return false;
        }
        String trimmedPhone = phone.trim();
        if (!PHONE_PATTERN.matcher(trimmedPhone).matches()) {
            return false;
        }
        String digitsOnly = trimmedPhone.replaceAll("\\D", "");
        return digitsOnly.length() >= MIN_PHONE_DIGITS;
    }

    public static boolean isValidName(String name) {
        if (!isNotNullOrEmpty(name)) {
            return false;
        }

        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidInteger(String numberString) {
        if (!isNotNullOrEmpty(numberString)) {
            return false;
        }
        try {
            Integer.parseInt(numberString.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidDouble(String numberString) {
        if (!isNotNullOrEmpty(numberString)) {
            return false;
        }
        try {
            Double.parseDouble(numberString.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void main (String []args) {

    }
}

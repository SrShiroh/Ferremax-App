package com.ferremax.util;

public class Validations {
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        String phoneRegex = "^\\d{9,15}$";
        return phoneNumber.matches(phoneRegex);
    }

    public static boolean isValidTime(String hour) {
        if (hour == null || hour.isEmpty()) {
            return true;
        }
        String hourRegex = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        return !hour.matches(hourRegex);
    }
}

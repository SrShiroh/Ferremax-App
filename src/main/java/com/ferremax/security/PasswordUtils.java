package com.ferremax.security;

public class PasswordUtils {

    /**
     * Método simplificado para comparar contraseñas en texto plano
     */
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        return inputPassword != null && inputPassword.equals(storedPassword);
    }
}

package com.ferremax.security;

import com.ferremax.model.RolUsuario;
import com.ferremax.model.Usuario;

public class SessionManager {
    private static Usuario loggedInUser = null;

    public static void setLoggedInUser(Usuario user) {
        loggedInUser = user;
    }

    public static Usuario getLoggedInUser() {
        return loggedInUser;
    }

    public static String getLoggedInUserName() {
        return (loggedInUser != null) ? loggedInUser.getNombre() : "";
    }

    public static RolUsuario getLoggedInUserRole() {
        return (loggedInUser != null) ? loggedInUser.getRol() : null;
    }

    public static boolean checkPermission(String permission) {
        if (loggedInUser == null) return false;
        return loggedInUser.hasPermission(permission);
    }

    public static void clearSession() {
        loggedInUser = null;
    }
}

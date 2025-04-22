package com.ferremax.security;

import com.ferremax.model.Usuario;
import com.ferremax.model.RolUsuario;

public class SessionManager {

    private static Usuario loggedInUser = null;

    public static void login(Usuario user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario para iniciar sesión no puede ser nulo.");
        }
        loggedInUser = user;
        System.out.println("INFO: Sesión iniciada para el usuario: " + user.getNombre() + " con rol: " + user.getRol());
    }

    public static void logout() {
        if (loggedInUser != null) {
            System.out.println("INFO: Cerrando sesión para el usuario: " + loggedInUser.getNombre());
            loggedInUser = null;
        } else {
            System.out.println("INFO: No hay sesión activa para cerrar.");
        }
    }

    public static Usuario getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static boolean hasRole(RolUsuario rol) {
        return isLoggedIn() && loggedInUser.getRol() == rol;
    }

    public static int getLoggedInUserId() {
        return isLoggedIn() ? loggedInUser.getId() : -1;
    }

    public static String getLoggedInUserName() {
        return isLoggedIn() ? loggedInUser.getNombre() : null;
    }

    public static RolUsuario getLoggedInUserRole() {
        return isLoggedIn() ? loggedInUser.getRol() : null;
    }
}

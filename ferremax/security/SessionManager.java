package com.ferremax.security;

import com.ferremax.model.Usuario;

public class SessionManager {
    private static Usuario loggedInUser = null;

    public static void setLoggedInUser(Usuario user) {
        loggedInUser = user;
    }

    public static Usuario getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        loggedInUser = null;
    }
}

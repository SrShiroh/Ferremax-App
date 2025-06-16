package com.ferremax.controller;

import com.ferremax.dao.UsuarioDAO;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Usuario;
import com.ferremax.security.SessionManager;
import com.ferremax.gui.AdminMainFrame;
import com.ferremax.gui.EmployeeMainFrame;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.JFrame;

public class LoginController {
    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public static Usuario getUsuarioLogueado() {
        return SessionManager.getLoggedInUser();
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        Usuario user = usuarioDAO.findByUsername(username);
        if (user == null || !user.isActivo()) {
            return false;
        }

        if (!BCrypt.checkpw(password, user.getContrasena())) {
            return false;
        }

        SessionManager.setLoggedInUser(user);
        usuarioDAO.updateLastLogin(user.getId());
        return true;
    }

    public JFrame redirectToProperView() {
        Usuario user = SessionManager.getLoggedInUser();
        if (user == null) {
            return null;
        }

        RolUsuario rol = user.getRol();
        return switch (rol) {
            case ADMINISTRADOR -> new AdminMainFrame();
            case EMPLEADO, TECNICO -> new EmployeeMainFrame();
        };
    }

    public static void logout() {
        SessionManager.clearSession();
    }
}

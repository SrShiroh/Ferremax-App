package com.ferremax;

import com.ferremax.gui.AdminRegister;
import com.ferremax.gui.LoginFrame;
import com.ferremax.dao.UsuarioDAO;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        configureLookAndFeel();

        if (UsuarioDAO.checkAdminExists()) {
            java.awt.EventQueue.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        } else {
            java.awt.EventQueue.invokeLater(() -> {
                new AdminRegister().setVisible(true);
            });
        }

    }

    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.background", new java.awt.Color(0, 123, 255));
            UIManager.put("Button.foreground", java.awt.Color.WHITE);
            UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        } catch (Exception e) {
            System.err.println("Error al configurar Look and Feel: " + e.getMessage());
        }
    }
}

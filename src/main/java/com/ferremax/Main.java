package com.ferremax;

import com.ferremax.db.DatabaseConnection;
import com.ferremax.gui.LoginFrame;
import com.ferremax.util.ExceptionHandler;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar look and feel
        configureLookAndFeel();

        // Verificar conexión a base de datos
        try {
            DatabaseConnection.testConnection();
            System.out.println("Conexión a base de datos establecida correctamente.");
        } catch (Exception e) {
            ExceptionHandler.showError("Error al conectar con la base de datos: " + e.getMessage(), "Error de Conexión");
            System.exit(1);
        }

        // Iniciar aplicación con pantalla de login
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }

    private static void configureLookAndFeel() {
        try {
            // Intentar usar look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Personalizar componentes comunes
            UIManager.put("Button.background", new java.awt.Color(0, 123, 255));
            UIManager.put("Button.foreground", java.awt.Color.WHITE);
            UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        } catch (Exception e) {
            System.err.println("Error al configurar Look and Feel: " + e.getMessage());
        }
    }
}

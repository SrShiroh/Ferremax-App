package com.ferremax.gui;

import com.ferremax.model.RolUsuario;
import com.ferremax.model.Usuario;
import com.ferremax.security.SessionManager;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {

    protected Usuario currentUser;

    public BasePanel() {
        currentUser = SessionManager.getLoggedInUser();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        if (currentUser == null) {
            showErrorMessage("No hay usuario en sesión");
            return;
        }

        initComponents();
        setupListeners();
    }

    protected abstract void initComponents();
    protected abstract void setupListeners();

    protected boolean checkPermission(String permission) {
        return currentUser != null && currentUser.hasPermission(permission);
    }

    protected void showErrorMessage(String message) {
        removeAll();
        add(new JLabel(message, JLabel.CENTER), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    protected JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        return headerPanel;
    }

    protected JButton createStandardButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    protected JTextField createStandardTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    protected boolean isAdmin() {
        return currentUser != null && currentUser.getRol() == RolUsuario.ADMINISTRADOR;
    }

    protected boolean isTecnico() {
        return currentUser != null && currentUser.getRol() == RolUsuario.TECNICO;
    }
}

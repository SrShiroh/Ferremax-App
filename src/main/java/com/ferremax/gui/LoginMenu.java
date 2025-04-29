package com.ferremax.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginMenu extends JFrame{
    private JTextField campoUser;
    private JPasswordField campoPassword;

    public LoginMenu() {
        super("Inicio de Sesión");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("Login", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panelPrincipal.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Usuario"), gbc);

        campoUser = new JTextField(10);
        gbc.gridx = 1;
        panelPrincipal.add(campoUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Contraseña"), gbc);

        campoPassword = new JPasswordField(10);
        gbc.gridx = 1;
        panelPrincipal.add(campoPassword, gbc);

        JButton botonLogin = new JButton("Iniciar Sesión");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        panelPrincipal.add(botonLogin, gbc);

        botonLogin.addActionListener(e -> {
            
        });
        add(panelPrincipal);
        setVisible(true);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.setVisible(true);
        });
    }
}

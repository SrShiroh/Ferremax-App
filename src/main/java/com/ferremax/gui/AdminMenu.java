package com.ferremax.gui;

import javax.swing.*;
import java.awt.*;

public class AdminMenu extends JFrame{
    private JTextField campoUser;
    private JPasswordField campoPassword;

    public AdminMenu() {
        super("Panel de Administración");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("Gestor de actividades", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelPrincipal.add(titulo, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JButton("Gestion Clientes"), gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelPrincipal.add(new JButton("Gestion Solicitudes"), gbc);

        gbc.gridwidth = 3;
        gbc.gridx = 2;
        gbc.gridy = 2;
        panelPrincipal.add(new JButton("Gestion Empleados"));

        gbc.gridwidth = 3;
        gbc.gridx = 2;
        gbc.gridy = 4;
        panelPrincipal.add(new JButton("Gestion Usuarios"));

        gbc.gridwidth = 4;
        gbc.gridy = 3;
        gbc.gridx = 3;
        panelPrincipal.add(new JButton("Gestion Usuarios"));

        add(panelPrincipal);
        setVisible(true);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminMenu adminMenu = new AdminMenu();
        });
    }
}

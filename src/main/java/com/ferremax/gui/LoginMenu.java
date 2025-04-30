package com.ferremax.gui;

import javax.swing.*;
import java.awt.*;
import com.ferremax.util.ValidationUtils;
import com.ferremax.dao.UsuarioDAO;

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
            if (ValidationUtils.isValidName(campoUser.getText())){
                String user = campoUser.getText();
                String pass = campoPassword.getText();
                //Valida los campos user y pass en la db.
                if (ValidationUtils.isValidName(user) && ValidationUtils.isValidName(pass)){
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    if (usuarioDAO.validarCredenciales(user, pass)){
                        JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                       // Siguiente frame (terminar xde)
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña inválida", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        add(panelPrincipal);
        setVisible(true);

    }
}

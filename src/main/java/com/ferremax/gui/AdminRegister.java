package com.ferremax.gui;

import com.ferremax.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;

public class AdminRegister extends JFrame {
    private JTextField txtNombre;
    private JTextField txtUsuario;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnRegistrar;
    private JButton btnSalir;

    public AdminRegister() {
        super("Sistema de Gestión de Reparaciones AC");
        initComponents();
        configurarVentana();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));

        JPanel loginPanel = createRegisterPanel();
        centerPanel.add(loginPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 33, 33));
        panel.setPreferredSize(new Dimension(800, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblLogo = new JLabel("FERREMAX");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        panel.add(lblLogo, BorderLayout.WEST);

        JLabel lblTitle = new JLabel("SISTEMA DE GESTIÓN DE REPARACIONES AC");
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTitle.setForeground(new Color(180, 180, 180));
        lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblTitle, BorderLayout.EAST);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        panel.setPreferredSize(new Dimension(400, 450));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblRegisterTitle = new JLabel("Registrar Administrador");
        lblRegisterTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblRegisterTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblRegisterTitle, gbc);

        JLabel lblIcono;
        try {
            java.net.URL imageUrl = getClass().getResource("/images/login-icon.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblIcono = new JLabel(new ImageIcon(img));
            } else {
                lblIcono = new JLabel("👤");
                lblIcono.setFont(new Font("Arial", Font.PLAIN, 60));
                lblIcono.setForeground(new Color(52, 152, 219));
            }
        } catch (Exception e) {
            lblIcono = new JLabel("👤");
            lblIcono.setFont(new Font("Arial", Font.PLAIN, 60));
            lblIcono.setForeground(new Color(52, 152, 219));
        }
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(lblIcono, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel lblNombre = new JLabel("Nombre Completo");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 10);
        panel.add(lblNombre, gbc);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(txtNombre, gbc);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 10);
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(txtUsuario, gbc);

        JLabel lblCorreo = new JLabel("Correo Electrónico");
        lblCorreo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 10);
        panel.add(lblCorreo, gbc);

        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(txtCorreo, gbc);

        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 10);
        panel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(txtContrasena, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(52, 152, 219));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(108, 117, 125));
        btnSalir.setForeground(Color.BLACK);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnRegistrar);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnSalir);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(buttonPanel, gbc);

        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (nombre.isEmpty() || usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (usuarioDAO.registrarAdministrador(usuario, nombre, correo, contrasena)) {
                JOptionPane.showMessageDialog(this, "Administrador registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtNombre.setText("");
                txtUsuario.setText("");
                txtCorreo.setText("");
                txtContrasena.setText("");

                this.dispose();
                new LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el administrador. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalir.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblCopyright = new JLabel("© 2025 Ferremax - Coded by Santiago & Facundo");
        lblCopyright.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCopyright.setForeground(new Color(100, 100, 100));
        panel.add(lblCopyright, BorderLayout.WEST);

        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(100, 100, 100));
        panel.add(lblVersion, BorderLayout.EAST);

        return panel;
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            java.net.URL iconUrl = getClass().getResource("/images/app-icon.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception e) {
        }
    }
}
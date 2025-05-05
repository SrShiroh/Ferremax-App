package com.ferremax.gui;

import com.ferremax.controller.LoginController;
import com.ferremax.util.ExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnSalir;
    private LoginController controller;

    public LoginFrame() {
        super("Sistema de Gestión de Reparaciones AC");
        controller = new LoginController();
        initComponents();
        setupListeners();
        configurarVentana();
    }

    private void initComponents() {
        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel de cabecera con logo y título
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central que contiene el formulario de login
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));

        // Panel de login con diseño moderno
        JPanel loginPanel = createLoginPanel();
        centerPanel.add(loginPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel de pie de página
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 33, 33));
        panel.setPreferredSize(new Dimension(800, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Logo de la empresa
        JLabel lblLogo = new JLabel("FERREMAX");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        panel.add(lblLogo, BorderLayout.WEST);

        // Título del sistema
        JLabel lblTitle = new JLabel("SISTEMA DE GESTIÓN DE REPARACIONES AC");
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTitle.setForeground(new Color(180, 180, 180));
        lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblTitle, BorderLayout.EAST);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        panel.setPreferredSize(new Dimension(400, 400));

        // Título del panel de login
        JLabel lblLoginTitle = new JLabel("Iniciar Sesión");
        lblLoginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblLoginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblLoginTitle);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Icono de usuario
        JLabel lblIcono;
        try {
            // Intentar cargar la imagen, si existe
            java.net.URL imageUrl = getClass().getResource("/images/login-icon.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                // Escalar la imagen si es necesario
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblIcono = new JLabel(new ImageIcon(img));
            } else {
                // Usar un texto como alternativa
                lblIcono = new JLabel("👤");
                lblIcono.setFont(new Font("Arial", Font.PLAIN, 60));
                lblIcono.setForeground(new Color(52, 152, 219));
            }
        } catch (Exception e) {
            lblIcono = new JLabel("👤");
            lblIcono.setFont(new Font("Arial", Font.PLAIN, 60));
            lblIcono.setForeground(new Color(52, 152, 219));
        }

        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcono);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Campo de usuario
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setOpaque(false);
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.setMaximumSize(new Dimension(300, 60));

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        userPanel.add(lblUsuario);

        userPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        userPanel.add(txtUsuario);

        panel.add(userPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Campo de contraseña
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setOpaque(false);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.setMaximumSize(new Dimension(300, 60));

        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        passwordPanel.add(lblContrasena);

        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        passwordPanel.add(txtContrasena);

        panel.add(passwordPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(300, 40));

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(52, 152, 219));
        btnIngresar.setForeground(Color.BLACK);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(108, 117, 125));
        btnSalir.setForeground(Color.BLACK);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnIngresar);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnSalir);

        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblCopyright = new JLabel("© 2025 Ferremax - Todos los derechos reservados");
        lblCopyright.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCopyright.setForeground(new Color(100, 100, 100));
        panel.add(lblCopyright, BorderLayout.WEST);

        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(100, 100, 100));
        panel.add(lblVersion, BorderLayout.EAST);

        return panel;
    }

    private void setupListeners() {
        btnIngresar.addActionListener(e -> autenticar());

        btnSalir.addActionListener(e -> System.exit(0));

        // Al presionar Enter en el campo de contraseña, también autenticar
        txtContrasena.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    autenticar();
                }
            }
        });

        // Al presionar Enter en el campo de usuario, pasar al campo de contraseña
        txtUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtContrasena.requestFocus();
                }
            }
        });
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);  // Centrar en pantalla
        setResizable(false);

        // Configurar icono de la aplicación si existe
        try {
            java.net.URL iconUrl = getClass().getResource("/images/app-icon.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception e) {
            // Si no se puede cargar el icono, continuar sin él
        }
    }

    private void autenticar() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            ExceptionHandler.showWarning("Por favor ingrese usuario y contraseña", "Datos faltantes");
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (controller.authenticate(usuario, contrasena)) {
                JFrame mainFrame = controller.redirectToProperView();
                if (mainFrame != null) {
                    mainFrame.setVisible(true);
                    dispose();  // Cerrar ventana de login
                } else {
                    ExceptionHandler.showError("Error al cargar la ventana principal", "Error");
                }
            } else {
                ExceptionHandler.showError("Usuario o contraseña incorrectos", "Error de autenticación");
                txtContrasena.setText("");
                txtContrasena.requestFocus();
            }
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}

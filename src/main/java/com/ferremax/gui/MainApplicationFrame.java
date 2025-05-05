package com.ferremax.gui;

import com.ferremax.controller.LoginController;
import com.ferremax.model.Usuario;
import com.ferremax.security.SessionManager;
import com.ferremax.util.ExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class MainApplicationFrame extends JFrame {

    protected JPanel mainPanel;
    protected JPanel contentPanel;
    protected JPanel sidebarPanel;
    protected LoginController loginController;
    protected Usuario currentUser;

    public MainApplicationFrame(String title) {
        super(title);

        // Verificar que hay un usuario en sesión
        currentUser = SessionManager.getLoggedInUser();
        if (currentUser == null) {
            ExceptionHandler.showError("No hay usuario en sesión", "Error de Acceso");
            dispose();
            return;
        }

        loginController = new LoginController();

        // Inicializar componentes básicos
        initBaseComponents();
        setupBaseListeners();

        // Configurar cierre de ventana
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSesion();
            }
        });

        // Configurar ventana
        setSize(1024, 768);
        setLocationRelativeTo(null);
    }

    private void initBaseComponents() {
        // Panel principal
        mainPanel = new JPanel(new BorderLayout());

        // Barra superior
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Panel lateral de navegación
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(33, 37, 41));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Panel de contenido principal
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 123, 255));
        topBar.setPreferredSize(new Dimension(0, 50));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // Título de la aplicación
        JLabel lblAppTitle = new JLabel("Sistema de Reparaciones AC");
        lblAppTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblAppTitle.setForeground(Color.WHITE);
        topBar.add(lblAppTitle, BorderLayout.WEST);

        // Panel de usuario en la parte derecha
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel lblUser = new JLabel(currentUser.getNombre() + " (" + currentUser.getRol() + ")");
        lblUser.setForeground(Color.WHITE);
        userPanel.add(lblUser);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> cerrarSesion());
        userPanel.add(btnLogout);

        topBar.add(userPanel, BorderLayout.EAST);

        return topBar;
    }

    protected JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBackground(new Color(33, 37, 41));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(57, 63, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(33, 37, 41));
            }
        });

        return btn;
    }

    private void setupBaseListeners() {
        // Implementado por las clases hijas
    }

    protected void cerrarSesion() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar su sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            loginController.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    protected void setContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    protected abstract void initSpecificComponents();
    protected abstract void setupSpecificListeners();
}

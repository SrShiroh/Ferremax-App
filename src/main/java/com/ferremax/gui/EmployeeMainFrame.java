package com.ferremax.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmployeeMainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Constantes para los nombres de paneles
    private static final String PANEL_INICIO = "INICIO";
    private static final String PANEL_SOLICITUDES = "SOLICITUDES";
    private static final String PANEL_HORARIOS = "HORARIOS";
    private static final String PANEL_CREDENCIALES = "CREDENCIALES";

    public EmployeeMainFrame() {
        super("Sistema de Gestión de Reparaciones - Panel de Empleado");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Panel principal con diseño BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 246, 250));

        // Panel superior con información del usuario y logo
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel lateral con menú de navegación
        JPanel sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Panel central con CardLayout para mostrar diferentes secciones
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 246, 250));

        // Añadir los paneles al contenedor con CardLayout
        contentPanel.add(createHomePanel(), PANEL_INICIO);
        contentPanel.add(createSolicitudesPanel(), PANEL_SOLICITUDES);
        contentPanel.add(createHorariosPanel(), PANEL_HORARIOS);
        contentPanel.add(createCredencialesPanel(), PANEL_CREDENCIALES);

        // Mostrar el panel inicial
        cardLayout.show(contentPanel, PANEL_INICIO);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel inferior con información de la empresa
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 123, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Logo o nombre de la empresa (lado izquierdo)
        JLabel lblLogo = new JLabel("FERREMAX");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);
        panel.add(lblLogo, BorderLayout.WEST);

        // Panel con información del usuario y botón de cerrar sesión (lado derecho)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel lblUser = new JLabel("Empleado: Juan Pérez");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        userPanel.add(lblUser);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        userPanel.add(btnLogout);

        panel.add(userPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 58, 64));
        panel.setPreferredSize(new Dimension(220, getHeight()));

        // Espacio en la parte superior
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Añadir ítems del menú
        addMenuItem(panel, "Inicio", PANEL_INICIO, "home");
        addMenuItem(panel, "Gestión de Solicitudes", PANEL_SOLICITUDES, "file-text");
        addMenuItem(panel, "Gestión de Horarios", PANEL_HORARIOS, "calendar");
        addMenuItem(panel, "Mis Credenciales", PANEL_CREDENCIALES, "user");

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void addMenuItem(JPanel panel, String title, String panelName, String icon) {
        JPanel menuItem = new JPanel(new BorderLayout());
        menuItem.setBackground(new Color(52, 58, 64));
        menuItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        menuItem.add(lblTitle, BorderLayout.CENTER);

        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, panelName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(73, 80, 87));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(new Color(52, 58, 64));
            }
        });

        panel.add(menuItem);
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(233, 236, 239));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblCopyright = new JLabel("© 2025 Ferremax - Sistema de Gestión de Aires Acondicionados");
        lblCopyright.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblCopyright, BorderLayout.WEST);

        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Panel de bienvenida
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(248, 249, 250));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblWelcome = new JLabel("Bienvenido al Sistema de Gestión de Reparaciones");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(lblWelcome, gbc);

        gbc.insets = new Insets(20, 0, 0, 0);
        JLabel lblSubtitle = new JLabel("Panel de Empleado");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomePanel.add(lblSubtitle, gbc);

        gbc.insets = new Insets(40, 0, 0, 0);
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatCard("Solicitudes Pendientes", "5", new Color(0, 123, 255)));
        statsPanel.add(createStatCard("Horarios Disponibles", "12", new Color(40, 167, 69)));

        welcomePanel.add(statsPanel, gbc);

        panel.add(welcomePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(accentColor);
        headerPanel.setPreferredSize(new Dimension(panel.getWidth(), 5));
        panel.add(headerPanel, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        panel.add(lblTitle, BorderLayout.SOUTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 36));
        lblValue.setHorizontalAlignment(JLabel.CENTER);
        lblValue.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSolicitudesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y buscador
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Gestión de Solicitudes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JTextField txtSearch = new JTextField(20);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(0, 123, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabla de solicitudes
        String[] columnNames = {"ID", "Solicitante", "Contacto", "Dirección", "Fecha Programada", "Estado", "Acciones"};
        Object[][] data = {
                {"S001", "Juan García", "555-1234", "Calle Principal #123", "05/05/2025 09:00", "Pendiente", ""},
                {"S002", "Ana Martínez", "555-5678", "Av. Central #456", "05/05/2025 11:30", "Asignada", ""},
                {"S003", "Carlos López", "555-9012", "Plaza Mayor #789", "07/05/2025 09:00", "En Proceso", ""},
                {"S004", "María Torres", "555-3456", "Calle Norte #101", "08/05/2025 15:00", "Pendiente", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Renderizador para estado (colorear según estado)
        table.getColumnModel().getColumn(5).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);

            switch (value.toString()) {
                case "Pendiente":
                    label.setBackground(new Color(255, 243, 205));
                    label.setForeground(new Color(133, 100, 4));
                    break;
                case "Asignada":
                    label.setBackground(new Color(209, 236, 241));
                    label.setForeground(new Color(12, 84, 96));
                    break;
                case "En Proceso":
                    label.setBackground(new Color(225, 236, 250));
                    label.setForeground(new Color(0, 64, 133));
                    break;
                case "Completada":
                    label.setBackground(new Color(212, 237, 218));
                    label.setForeground(new Color(21, 87, 36));
                    break;
                default:
                    label.setBackground(Color.WHITE);
                    break;
            }

            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return label;
        });

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            JButton btnVer = new JButton("Ver");
            btnVer.setBackground(new Color(0, 123, 255));
            btnVer.setForeground(Color.WHITE);
            btnVer.setFocusPainted(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(255, 193, 7));
            btnEditar.setForeground(Color.BLACK);
            btnEditar.setFocusPainted(false);

            buttonPanel.add(btnVer);
            buttonPanel.add(btnEditar);

            return buttonPanel;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones en la parte inferior
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionsPanel.setOpaque(false);

        JButton btnNueva = new JButton("Nueva Solicitud");
        btnNueva.setBackground(new Color(40, 167, 69));
        btnNueva.setForeground(Color.WHITE);
        btnNueva.setFocusPainted(false);
        btnNueva.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnExportar = new JButton("Exportar a Excel");
        btnExportar.setBackground(new Color(23, 162, 184));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);

        actionsPanel.add(btnNueva);
        actionsPanel.add(btnExportar);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHorariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y selector de fechas
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Gestión de Horarios");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de filtros de fecha
        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dateFilterPanel.setOpaque(false);

        dateFilterPanel.add(new JLabel("Ver horarios desde:"));
        JTextField txtFecha = new JTextField(10);
        txtFecha.setText("05/05/2025");
        dateFilterPanel.add(txtFecha);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(new Color(0, 123, 255));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        dateFilterPanel.add(btnFiltrar);

        headerPanel.add(dateFilterPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con vista de horarios
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Vista en forma de tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        String[] columnNames = {"ID", "Fecha", "Hora Inicio", "Hora Fin", "Disponible", "Cliente", "Acciones"};
        Object[][] data = {
                {"H001", "05/05/2025", "09:00", "11:00", "No", "Juan García", ""},
                {"H002", "05/05/2025", "11:30", "13:30", "No", "Ana Martínez", ""},
                {"H003", "06/05/2025", "09:00", "11:00", "Sí", "-", ""},
                {"H004", "06/05/2025", "11:30", "13:30", "Sí", "-", ""},
                {"H005", "07/05/2025", "09:00", "11:00", "No", "Carlos López", ""},
                {"H006", "07/05/2025", "11:30", "13:30", "Sí", "-", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Renderizador para disponibilidad
        table.getColumnModel().getColumn(4).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);

            if ("Sí".equals(value.toString())) {
                label.setBackground(new Color(212, 237, 218));
                label.setForeground(new Color(21, 87, 36));
            } else {
                label.setBackground(new Color(248, 215, 218));
                label.setForeground(new Color(114, 28, 36));
            }

            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return label;
        });

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            String disponible = (String) table1.getValueAt(row, 4);

            if ("Sí".equals(disponible)) {
                JButton btnAsignar = new JButton("Asignar");
                btnAsignar.setBackground(new Color(40, 167, 69));
                btnAsignar.setForeground(Color.WHITE);
                btnAsignar.setFocusPainted(false);
                buttonPanel.add(btnAsignar);
            } else {
                JButton btnVer = new JButton("Ver Detalles");
                btnVer.setBackground(new Color(0, 123, 255));
                btnVer.setForeground(Color.WHITE);
                btnVer.setFocusPainted(false);
                buttonPanel.add(btnVer);
            }

            return buttonPanel;
        });

        JScrollPane tableScrollPane = new JScrollPane(table);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Vista de calendario (simulada)
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(Color.WHITE);

        JPanel weekView = new JPanel(new GridLayout(0, 5, 5, 5));
        weekView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        weekView.setBackground(Color.WHITE);

        // Encabezados de los días
        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        for (String day : days) {
            JLabel lblDay = new JLabel(day, JLabel.CENTER);
            lblDay.setFont(new Font("Arial", Font.BOLD, 14));
            lblDay.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
            lblDay.setPreferredSize(new Dimension(150, 40));
            weekView.add(lblDay);
        }

        // Fechas del calendario
        String[] dates = {"05/05", "06/05", "07/05", "08/05", "09/05"};
        for (String date : dates) {
            JLabel lblDate = new JLabel(date, JLabel.CENTER);
            lblDate.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
            weekView.add(lblDate);
        }

        // Horas y eventos
        String[] timeSlots = {"9:00-11:00", "11:30-13:30", "14:00-16:00", "16:30-18:30"};

        for (String timeSlot : timeSlots) {
            // Añadir la etiqueta de hora a la izquierda
            for (int i = 0; i < 5; i++) { // 5 días
                JPanel slotPanel = new JPanel(new BorderLayout());
                slotPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

                if (i == 0 && timeSlot.equals("9:00-11:00") ||
                        i == 0 && timeSlot.equals("11:30-13:30") ||
                        i == 2 && timeSlot.equals("9:00-11:00")) {
                    // Estas son citas ocupadas
                    slotPanel.setBackground(new Color(248, 215, 218));
                    JLabel lblAppointment = new JLabel("<html><center>Ocupado<br>Cliente: " +
                            (i == 0 && timeSlot.equals("9:00-11:00") ? "Juan García" :
                                    i == 0 && timeSlot.equals("11:30-13:30") ? "Ana Martínez" : "Carlos López") +
                            "</center></html>", JLabel.CENTER);
                    slotPanel.add(lblAppointment, BorderLayout.CENTER);
                } else {
                    slotPanel.setBackground(new Color(212, 237, 218));
                    JLabel lblFree = new JLabel("Disponible", JLabel.CENTER);
                    slotPanel.add(lblFree, BorderLayout.CENTER);

                    JButton btnReservar = new JButton("Reservar");
                    btnReservar.setBackground(new Color(40, 167, 69));
                    btnReservar.setForeground(Color.WHITE);
                    btnReservar.setFocusPainted(false);
                    slotPanel.add(btnReservar, BorderLayout.SOUTH);
                }

                weekView.add(slotPanel);
            }
        }

        JScrollPane calendarScrollPane = new JScrollPane(weekView);
        calendarPanel.add(calendarScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Vista Tabla", tablePanel);
        tabbedPane.addTab("Vista Calendario", calendarPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        // Panel de acciones en la parte inferior
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionsPanel.setOpaque(false);

        JButton btnCrear = new JButton("Crear Horario");
        btnCrear.setBackground(new Color(40, 167, 69));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 14));

        actionsPanel.add(btnCrear);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCredencialesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        "Actualizar Mis Credenciales"
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Información actual del usuario
        JPanel userInfoPanel = new JPanel(new GridBagLayout());
        userInfoPanel.setBackground(new Color(248, 249, 250));
        userInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.insets = new Insets(5, 5, 5, 5);
        infoGbc.fill = GridBagConstraints.HORIZONTAL;
        infoGbc.anchor = GridBagConstraints.WEST;

        infoGbc.gridx = 0;
        infoGbc.gridy = 0;
        userInfoPanel.add(new JLabel("Usuario:"), infoGbc);
        infoGbc.gridx = 1;
        JLabel lblUsuario = new JLabel("empleado1");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(lblUsuario, infoGbc);

        infoGbc.gridx = 0;
        infoGbc.gridy = 1;
        userInfoPanel.add(new JLabel("Nombre:"), infoGbc);
        infoGbc.gridx = 1;
        JLabel lblNombre = new JLabel("Juan Pérez");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(lblNombre, infoGbc);

        infoGbc.gridx = 0;
        infoGbc.gridy = 2;
        userInfoPanel.add(new JLabel("Rol:"), infoGbc);
        infoGbc.gridx = 1;
        JLabel lblRol = new JLabel("Empleado");
        lblRol.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(lblRol, infoGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(userInfoPanel, gbc);
        gbc.gridwidth = 1;

        // Correo electrónico
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("Correo Electrónico Actual:"), gbc);
        gbc.gridx = 1;
        JLabel lblCorreoActual = new JLabel("juan.perez@ferremax.com");
        lblCorreoActual.setFont(new Font("Arial", Font.ITALIC, 14));
        contentPanel.add(lblCorreoActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Nuevo Correo Electrónico (opcional):"), gbc);
        gbc.gridx = 1;
        JTextField txtNuevoCorreo = new JTextField(20);
        txtNuevoCorreo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(txtNuevoCorreo, gbc);

        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(new JLabel("Teléfono Actual:"), gbc);
        gbc.gridx = 1;
        JLabel lblTelefonoActual = new JLabel("555-123-4567");
        lblTelefonoActual.setFont(new Font("Arial", Font.ITALIC, 14));
        contentPanel.add(lblTelefonoActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(new JLabel("Nuevo Teléfono (opcional):"), gbc);
        gbc.gridx = 1;
        JTextField txtNuevoTelefono = new JTextField(20);
        txtNuevoTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(txtNuevoTelefono, gbc);

        // Separador
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Contraseñas
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(new JLabel("Contraseña Actual (*):"), gbc);
        gbc.gridx = 1;
        JPasswordField pwdActual = new JPasswordField(20);
        pwdActual.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(pwdActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(new JLabel("Nueva Contraseña (opcional):"), gbc);
        gbc.gridx = 1;
        JPasswordField pwdNueva = new JPasswordField(20);
        pwdNueva.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(pwdNueva, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        contentPanel.add(new JLabel("Confirmar Nueva Contraseña:"), gbc);
        gbc.gridx = 1;
        JPasswordField pwdConfirmar = new JPasswordField(20);
        pwdConfirmar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(pwdConfirmar, gbc);

        // Panel de botones
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnActualizar = new JButton("Actualizar Información");
        btnActualizar.setBackground(new Color(0, 123, 255));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.setPreferredSize(new Dimension(200, 40));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnCancelar);

        contentPanel.add(buttonPanel, gbc);

        // Añadir mensaje informativo
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("(*) Campo obligatorio para confirmar cambios");
        lblInfo.setForeground(new Color(108, 117, 125));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(lblInfo, gbc);

        // Panel principal centrado
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(contentPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeMainFrame());
    }
}
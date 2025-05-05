package com.ferremax.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminMainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Constantes para los nombres de paneles
    private static final String PANEL_INICIO = "INICIO";
    private static final String PANEL_CLIENTES = "CLIENTES";
    private static final String PANEL_SOLICITUDES = "SOLICITUDES";
    private static final String PANEL_EMPLEADOS = "EMPLEADOS";
    private static final String PANEL_HORARIOS = "HORARIOS";
    private static final String PANEL_USUARIOS = "USUARIOS";

    public AdminMainFrame() {
        super("Sistema de Gestión de Reparaciones - Panel de Administrador");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Panel principal con diseño BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel lateral con menú
        JPanel sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Panel de cabecera con información de usuario
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de contenido principal con CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Añadir los distintos paneles de contenido
        contentPanel.add(createHomePanel(), PANEL_INICIO);
        contentPanel.add(createClientesPanel(), PANEL_CLIENTES);
        contentPanel.add(createSolicitudesPanel(), PANEL_SOLICITUDES);
        contentPanel.add(createEmpleadosPanel(), PANEL_EMPLEADOS);
        contentPanel.add(createHorariosPanel(), PANEL_HORARIOS);
        contentPanel.add(createUsuariosPanel(), PANEL_USUARIOS);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel de estado en la parte inferior
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // Mostrar panel inicial
        cardLayout.show(contentPanel, PANEL_INICIO);

        add(mainPanel);
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(33, 33, 33));
        panel.setPreferredSize(new Dimension(220, getHeight()));

        // Logo o nombre de la empresa
        JLabel lblCompany = new JLabel("FERREMAX");
        lblCompany.setFont(new Font("Arial", Font.BOLD, 20));
        lblCompany.setForeground(Color.WHITE);
        lblCompany.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCompany.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));
        panel.add(lblCompany);

        // Separador
        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Opciones de menú
        addMenuItem(panel, "Dashboard", PANEL_INICIO, "Inicio");
        addMenuItem(panel, "Gestión de Clientes", PANEL_CLIENTES, "Clientes");
        addMenuItem(panel, "Gestión de Solicitudes", PANEL_SOLICITUDES, "Solicitudes");
        addMenuItem(panel, "Gestión de Empleados", PANEL_EMPLEADOS, "Empleados");
        addMenuItem(panel, "Gestión de Horarios", PANEL_HORARIOS, "Horarios");
        addMenuItem(panel, "Gestión de Usuarios", PANEL_USUARIOS, "Usuarios");

        panel.add(Box.createVerticalGlue());

        // Botón de cerrar sesión
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(200, 40));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.add(btnLogout);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        return panel;
    }

    private void addMenuItem(JPanel panel, String title, String panelName, String iconName) {
        JPanel menuItem = new JPanel(new BorderLayout());
        menuItem.setBackground(new Color(33, 33, 33));
        menuItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(Color.WHITE);
        menuItem.add(lblTitle, BorderLayout.CENTER);

        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, panelName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(66, 66, 66));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(new Color(33, 33, 33));
            }
        });

        panel.add(menuItem);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel("Panel de Administración");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitle, BorderLayout.WEST);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setOpaque(false);

        JLabel lblUsername = new JLabel("Admin");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfoPanel.add(lblUsername);

        panel.add(userInfoPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblStatus = new JLabel("© 2025 Ferremax - Todos los derechos reservados");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblStatus, BorderLayout.WEST);

        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);

        JLabel lblWelcome = new JLabel("Bienvenido al Panel de Administración");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblWelcome, BorderLayout.NORTH);

        // Dashboard principal con tarjetas de estadísticas
        JPanel dashboardPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        dashboardPanel.setOpaque(false);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        dashboardPanel.add(createStatCard("Solicitudes Pendientes", "24", new Color(231, 76, 60)));
        dashboardPanel.add(createStatCard("Empleados Activos", "8", new Color(52, 152, 219)));
        dashboardPanel.add(createStatCard("Clientes Registrados", "143", new Color(46, 204, 113)));
        dashboardPanel.add(createStatCard("Horarios Disponibles", "32", new Color(155, 89, 182)));

        panel.add(dashboardPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(accentColor);
        headerPanel.setPreferredSize(new Dimension(panel.getWidth(), 5));
        panel.add(headerPanel, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 36));
        lblValue.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblValue, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(lblTitle, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createClientesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y buscador
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Clientes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabla de clientes
        String[] columnNames = {"ID", "Nombre", "Correo", "Teléfono", "Dirección", "Acciones"};
        Object[][] data = {
                {"1", "Juan Pérez", "juan@example.com", "555-123-4567", "Av. Principal #123", ""},
                {"2", "María García", "maria@example.com", "555-987-6543", "Calle 42 #456", ""},
                {"3", "Carlos López", "carlos@example.com", "555-456-7890", "Plaza Central #789", ""},
                {"4", "Ana Martínez", "ana@example.com", "555-321-6540", "Av. Norte #101", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones es editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(5).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel1.setOpaque(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(52, 152, 219));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFocusPainted(false);

            panel1.add(btnEditar);
            panel1.add(btnEliminar);
            return panel1;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setOpaque(false);

        JButton btnAgregar = new JButton("Agregar Nuevo Cliente");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        actionsPanel.add(btnAgregar);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSolicitudesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y buscador
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Solicitudes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabla de solicitudes
        String[] columnNames = {"ID", "Solicitante", "Contacto", "Dirección", "Fecha", "Estado", "Acciones"};
        Object[][] data = {
                {"001", "Juan Pérez", "555-123-4567", "Av. Principal #123", "05/05/2025", "Pendiente", ""},
                {"002", "María García", "555-987-6543", "Calle 42 #456", "06/05/2025", "Asignada", ""},
                {"003", "Carlos López", "555-456-7890", "Plaza Central #789", "07/05/2025", "En Proceso", ""},
                {"004", "Ana Martínez", "555-321-6540", "Av. Norte #101", "08/05/2025", "Completada", ""}
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

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel1.setOpaque(false);

            JButton btnVer = new JButton("Ver");
            btnVer.setBackground(new Color(52, 152, 219));
            btnVer.setForeground(Color.WHITE);
            btnVer.setFocusPainted(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFocusPainted(false);

            panel1.add(btnVer);
            panel1.add(btnEditar);
            panel1.add(btnEliminar);
            return panel1;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setOpaque(false);

        JButton btnAgregar = new JButton("Nueva Solicitud");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        actionsPanel.add(btnAgregar);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEmpleadosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y opciones
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Empleados");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con tabla de empleados
        String[] columnNames = {"ID", "Nombre", "Correo", "Teléfono", "Rol", "Acciones"};
        Object[][] data = {
                {"E001", "Pedro Gómez", "pedro@ferremax.com", "555-111-2222", "Técnico", ""},
                {"E002", "Laura Sánchez", "laura@ferremax.com", "555-333-4444", "Empleado", ""},
                {"E003", "Roberto Díaz", "roberto@ferremax.com", "555-555-6666", "Técnico", ""},
                {"E004", "Sofía Torres", "sofia@ferremax.com", "555-777-8888", "Administrador", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones es editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(5).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            JButton btnDetalles = new JButton("Detalles");
            btnDetalles.setBackground(new Color(52, 152, 219));
            btnDetalles.setForeground(Color.WHITE);
            btnDetalles.setFocusPainted(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFocusPainted(false);

            buttonPanel.add(btnDetalles);
            buttonPanel.add(btnEditar);
            buttonPanel.add(btnEliminar);

            return buttonPanel;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones inferiores
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnAgregar = new JButton("Registrar Empleado");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setBackground(new Color(52, 152, 219));
        btnGenerarReporte.setForeground(Color.WHITE);
        btnGenerarReporte.setFocusPainted(false);
        btnGenerarReporte.setFont(new Font("Arial", Font.PLAIN, 14));

        actionsPanel.add(btnAgregar);
        actionsPanel.add(btnGenerarReporte);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHorariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y filtros de fecha
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Horarios");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de filtros de fecha
        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dateFilterPanel.setOpaque(false);

        dateFilterPanel.add(new JLabel("Desde:"));
        JTextField txtFechaInicio = new JTextField(10);
        dateFilterPanel.add(txtFechaInicio);

        dateFilterPanel.add(new JLabel("Hasta:"));
        JTextField txtFechaFin = new JTextField(10);
        dateFilterPanel.add(txtFechaFin);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(new Color(52, 152, 219));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        dateFilterPanel.add(btnFiltrar);

        headerPanel.add(dateFilterPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con tabla de horarios
        String[] columnNames = {"ID", "Fecha", "Hora Inicio", "Hora Fin", "Disponible", "Solicitud", "Técnico Asignado", "Acciones"};
        Object[][] data = {
                {"H001", "05/05/2025", "09:00", "11:00", "No", "S001", "Pedro Gómez", ""},
                {"H002", "05/05/2025", "11:30", "13:30", "No", "S002", "Roberto Díaz", ""},
                {"H003", "06/05/2025", "09:00", "11:00", "Sí", "-", "-", ""},
                {"H004", "06/05/2025", "11:30", "13:30", "Sí", "-", "-", ""},
                {"H005", "07/05/2025", "09:00", "11:00", "No", "S003", "Pedro Gómez", ""},
                {"H006", "07/05/2025", "11:30", "13:30", "Sí", "-", "-", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de acciones es editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(7).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFocusPainted(false);

            buttonPanel.add(btnEditar);
            buttonPanel.add(btnEliminar);

            return buttonPanel;
        });

        // Colorear filas según disponibilidad
        table.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setOpaque(true);

            if (column != 7) { // No aplicar a la columna de acciones
                String disponible = (String) table1.getValueAt(row, 4);
                if ("No".equals(disponible)) {
                    label.setBackground(new Color(255, 243, 224)); // Fondo naranja claro para no disponibles
                } else {
                    label.setBackground(new Color(232, 245, 233)); // Fondo verde claro para disponibles
                }
            }

            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

            return label;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones inferiores
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnAgregar = new JButton("Crear Horario");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnVerCalendario = new JButton("Ver Calendario");
        btnVerCalendario.setBackground(new Color(52, 152, 219));
        btnVerCalendario.setForeground(Color.WHITE);
        btnVerCalendario.setFocusPainted(false);
        btnVerCalendario.setFont(new Font("Arial", Font.PLAIN, 14));

        actionsPanel.add(btnAgregar);
        actionsPanel.add(btnVerCalendario);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        // Encabezado con título y buscador
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Usuarios");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Usuario: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con tabla de usuarios del sistema
        String[] columnNames = {"ID", "Usuario", "Nombre", "Correo", "Rol", "Último Acceso", "Estado", "Acciones"};
        Object[][] data = {
                {"1", "admin", "Admin Sistema", "admin@ferremax.com", "Administrador", "05/05/2025 00:15", "Activo", ""},
                {"2", "pedro", "Pedro Gómez", "pedro@ferremax.com", "Técnico", "04/05/2025 17:22", "Activo", ""},
                {"3", "laura", "Laura Sánchez", "laura@ferremax.com", "Empleado", "04/05/2025 16:45", "Activo", ""},
                {"4", "roberto", "Roberto Díaz", "roberto@ferremax.com", "Técnico", "03/05/2025 14:30", "Activo", ""},
                {"5", "miguel", "Miguel Ruiz", "miguel@ferremax.com", "Empleado", "28/04/2025 09:15", "Inactivo", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de acciones es editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(7).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            JButton btnResetPassword = new JButton("Reset");
            btnResetPassword.setBackground(new Color(52, 152, 219));
            btnResetPassword.setForeground(Color.WHITE);
            btnResetPassword.setFocusPainted(false);
            btnResetPassword.setToolTipText("Resetear Contraseña");

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);

            JButton btnToggleActive = new JButton("Activar/Desactivar");
            btnToggleActive.setBackground(new Color(142, 68, 173));
            btnToggleActive.setForeground(Color.WHITE);
            btnToggleActive.setFocusPainted(false);

            buttonPanel.add(btnResetPassword);
            buttonPanel.add(btnEditar);
            buttonPanel.add(btnToggleActive);

            return buttonPanel;
        });

        // Colorear filas según estado
        table.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
            if (column == 7) {
                return table.getColumnModel().getColumn(7).getCellRenderer().getTableCellRendererComponent(
                        table1, value, isSelected, hasFocus, row, column);
            }

            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setOpaque(true);

            String estado = (String) table1.getValueAt(row, 6);
            if ("Inactivo".equals(estado)) {
                label.setBackground(new Color(255, 236, 236)); // Fondo rojo claro para inactivos
                label.setForeground(Color.DARK_GRAY);
            } else {
                label.setBackground(Color.WHITE);
            }

            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

            return label;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones inferiores
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnAgregar = new JButton("Crear Usuario");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnPermisos = new JButton("Gestionar Permisos");
        btnPermisos.setBackground(new Color(52, 73, 94));
        btnPermisos.setForeground(Color.WHITE);
        btnPermisos.setFocusPainted(false);
        btnPermisos.setFont(new Font("Arial", Font.PLAIN, 14));

        actionsPanel.add(btnAgregar);
        actionsPanel.add(btnPermisos);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }
}


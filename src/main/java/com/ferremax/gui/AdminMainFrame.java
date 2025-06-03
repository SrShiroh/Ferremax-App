package com.ferremax.gui;

import com.ferremax.controller.LoginController;
import com.ferremax.model.*;
import com.ferremax.dao.*;
import com.ferremax.util.Validations;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminMainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private static final String PANEL_INICIO = "INICIO";
    private static final String PANEL_SOLICITUDES = "SOLICITUDES";
    private static final String PANEL_USUARIOS = "USUARIOS";
    private static final String PANEL_CLIENTES = "CLIENTES";

    private final Color accentColor = new Color(0, 123, 255);

    public AdminMainFrame() {
        super("Sistema de Gestión de Reparaciones - Panel de Administrador");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPanel.add(createHomePanel(), PANEL_INICIO);
        contentPanel.add(createSolicitudesPanel(), PANEL_SOLICITUDES);
        contentPanel.add(createUsuariosPanel(), PANEL_USUARIOS);
        contentPanel.add(createClientesPanel(), PANEL_CLIENTES);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        cardLayout.show(contentPanel, PANEL_INICIO);

        add(mainPanel);
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(33, 33, 33));
        panel.setPreferredSize(new Dimension(220, getHeight()));

        JLabel lblCompany = new JLabel("FERREMAX");
        lblCompany.setFont(new Font("Arial", Font.BOLD, 20));
        lblCompany.setForeground(Color.WHITE);
        lblCompany.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCompany.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));
        panel.add(lblCompany);

        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Cambiado el estilo de los botones del menú para que coincidan con la imagen
        addMenuItem(panel, "Dashboard", PANEL_INICIO, "Inicio");
        addMenuItem(panel, "Gestión de Solicitudes", PANEL_SOLICITUDES, "Solicitudes");
        addMenuItem(panel, "Gestión de Usuarios", PANEL_USUARIOS, "Usuarios");
        addMenuItem(panel, "Gestión de Clientes", PANEL_CLIENTES, "Clientes");

        panel.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(200, 40));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.add(btnLogout);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        btnLogout.addActionListener(e -> {
            Object originalMessageForeground = UIManager.get("OptionPane.messageForeground");
            Object originalButtonForeground = UIManager.get("Button.foreground");

            try {
                UIManager.put("OptionPane.messageForeground", Color.BLACK);
                UIManager.put("Button.foreground", Color.BLACK);

                int option = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de que desea cerrar sesión?",
                        "Cerrar Sesión",
                        JOptionPane.YES_NO_OPTION
                );

                if (option == JOptionPane.YES_OPTION) {
                    LoginController.logout();
                    dispose();
                    new LoginFrame().setVisible(true);
                }
            } finally {
                UIManager.put("OptionPane.messageForeground", originalMessageForeground);
                UIManager.put("Button.foreground", originalButtonForeground);
            }
        });

        return panel;
    }

    private void addMenuItem(JPanel panel, String title, String panelName, String iconName) {
        JPanel menuItem = new JPanel(new BorderLayout());
        menuItem.setBackground(new Color(33, 33, 33));
        menuItem.setBorder(BorderFactory.createCompoundBorder(
                // Agregado un borde visible para los botones del menú
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblTitle = new JLabel(title);
        // Aumentado el tamaño de la fuente
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
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
                // Agregar un borde al pasar el mouse
                menuItem.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 3, 0, 0, accentColor),
                        BorderFactory.createEmptyBorder(15, 12, 15, 15)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(new Color(33, 33, 33));
                // Restaurar el borde original
                menuItem.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        panel.add(menuItem);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 123, 255)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel("Panel de Administración");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitle, BorderLayout.WEST);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfoPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Actualizar Datos");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> actualizarTodasLasTablas());
        userInfoPanel.add(btnRefresh);

        JLabel lblUsername = new JLabel(LoginController.getUsuarioLogueado().getNombre());
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfoPanel.add(lblUsername);

        panel.add(userInfoPanel, BorderLayout.EAST);

        return panel;
    }

    private void actualizarTodasLasTablas() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        String vistaActual = null;
        for (Component comp : contentPanel.getComponents()) {
            if (comp.isVisible()) {
                vistaActual = comp.getName();
                break;
            }
        }

        final String panelActual = vistaActual;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    actualizarTablaSolicitudes();
                    actualizarTablaUsuarios();
                    actualizarEstadisticas(panelActual);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            AdminMainFrame.this,
                            "Error al actualizar datos: " + ex.getMessage(),
                            "Error de actualización",
                            JOptionPane.ERROR_MESSAGE
                    );
                    Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", ex);
                }
                return null;
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());

                JOptionPane.showMessageDialog(
                        AdminMainFrame.this,
                        "Todas las tablas han sido actualizadas correctamente",
                        "Actualización Exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (panelActual != null) {
                    cardLayout.show(contentPanel, panelActual);
                }
            }
        };

        worker.execute();
    }

    private void actualizarTablaSolicitudes() {
        int index = getComponentIndex(contentPanel, PANEL_SOLICITUDES);
        if (index != -1) {
            JPanel panelSolicitudes = (JPanel) contentPanel.getComponent(index);
            for (Component comp : panelSolicitudes.getComponents()) {
                if (comp instanceof JScrollPane scrollPane) {
                    if (scrollPane.getViewport().getView() instanceof JTable table) {
                        TableCellRenderer accionesRenderer = table.getColumnModel().getColumn(6).getCellRenderer();
                        TableCellEditor accionesEditor = table.getColumnModel().getColumn(6).getCellEditor();

                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.setDataVector(getSolicitudesTableData(),
                                new String[]{"ID", "Solicitante", "Contacto", "Dirección", "Fecha", "Estado", "Acciones"});

                        if (accionesRenderer != null) {
                            table.getColumnModel().getColumn(6).setCellRenderer(accionesRenderer);
                        }
                        if (accionesEditor != null) {
                            table.getColumnModel().getColumn(6).setCellEditor(accionesEditor);
                        }

                        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
                        table.getColumnModel().getColumn(6).setPreferredWidth(250); // Acciones

                        table.revalidate();
                        table.repaint();
                        break;
                    }
                }
            }
        }
    }

    private void actualizarEstadisticas(String vistaActual) {
        int index = getComponentIndex(contentPanel, PANEL_INICIO);
        if (index != -1) {
            JPanel nuevoPanel = createHomePanel();

            contentPanel.remove(index);
            contentPanel.add(nuevoPanel, PANEL_INICIO, index);

            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblStatus = new JLabel("© 2025 Ferremax - Coded by Santiago & Facundo");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblStatus, BorderLayout.WEST);

        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setName(PANEL_INICIO);

        JLabel lblWelcome = new JLabel("Bienvenido al Panel de Administración");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblWelcome, BorderLayout.NORTH);

        JPanel dashboardPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        dashboardPanel.setOpaque(false);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        dashboardPanel.add(createStatCard("Empleados Activos", String.valueOf(SolicitudDAO.getEmpleadosA()), new Color(231, 76, 60)));
        dashboardPanel.add(createStatCard("Solicitudes Pendientes", String.valueOf(SolicitudDAO.getSolicitudesP()), new Color(52, 152, 219)));
        dashboardPanel.add(createStatCard("Reparaciones Realizadas", String.valueOf(SolicitudDAO.getReparacionesR()), new Color(46, 204, 113)));
        dashboardPanel.add(createStatCard("Reparaciones Registradas", String.valueOf(SolicitudDAO.getReparacionesReg()), new Color(155, 89, 182)));

        panel.add(dashboardPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        // Agregado un borde más visible
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Aumentado el tamaño del panel para hacerlo más cuadrado
        panel.setPreferredSize(new Dimension(250, 200));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(accentColor);
        headerPanel.setPreferredSize(new Dimension(panel.getWidth(), 10));
        panel.add(headerPanel, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        // Aumentado el tamaño de la fuente
        lblValue.setFont(new Font("Arial", Font.BOLD, 48));
        lblValue.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblValue, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel(title);
        // Aumentado el tamaño de la fuente del título
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.SOUTH);

        return panel;
    }

    private Object[][] getSolicitudesTableData() {
        java.util.List<Object[]> rows = new ArrayList<>();

        java.util.List<Solicitud> allSolicitudes = SolicitudDAO.getSolicitudes();

        for (Solicitud solicitud : allSolicitudes) {
            Object[] row = new Object[7];
            row[0] = solicitud.getId();
            row[1] = solicitud.getSolicitante();
            row[2] = solicitud.getContacto();
            row[3] = solicitud.getDireccion();
            row[4] = solicitud.getFecha();
            row[5] = solicitud.getEstado();
            row[6] = "";
            rows.add(row);
        }

        return rows.toArray(new Object[0][]);
    }

    private JPanel createSolicitudesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setName(PANEL_SOLICITUDES);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Solicitudes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Solicitante", "Contacto", "Dirección", "Fecha", "Estado", "Acciones"};

        DefaultTableModel model = new DefaultTableModel(getSolicitudesTableData(), columnNames) {
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

        table.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 3, 8, 0));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            buttonPanel.setOpaque(false);

            JButton btnVer = new JButton("Ver");
            btnVer.setBackground(new Color(52, 152, 219));
            btnVer.setForeground(Color.BLACK);
            btnVer.setFocusPainted(false);
            btnVer.setMargin(new Insets(2, 8, 2, 8));

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.BLACK);
            btnEditar.setFocusPainted(false);
            btnEditar.setMargin(new Insets(2, 8, 2, 8));

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(142, 68, 173));
            btnEliminar.setForeground(Color.BLACK);
            btnEliminar.setFocusPainted(false);
            btnEliminar.setMargin(new Insets(2, 8, 2, 8));

            buttonPanel.add(btnVer);
            buttonPanel.add(btnEditar);
            buttonPanel.add(btnEliminar);

            return buttonPanel;
        });

        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()) {

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JPanel panel1 = new JPanel();
                panel1.setLayout(new GridLayout(1, 3, 8, 0));
                panel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                panel1.setOpaque(false);

                int solicitudId = Integer.parseInt(table.getValueAt(row, 0).toString());

                JButton btnVer = new JButton("Ver");
                btnVer.setBackground(new Color(52, 152, 219));
                btnVer.setForeground(Color.BLACK);
                btnVer.setFocusPainted(false);
                btnVer.setMargin(new Insets(2, 8, 2, 8));
                btnVer.addActionListener(e -> {
                    verSolicitud(solicitudId);
                    fireEditingStopped();
                });

                JButton btnEditar = new JButton("Editar");
                btnEditar.setBackground(new Color(243, 156, 18));
                btnEditar.setForeground(Color.BLACK);
                btnEditar.setFocusPainted(false);
                btnEditar.setMargin(new Insets(2, 8, 2, 8));
                btnEditar.addActionListener(e -> {
                    editarSolicitud(solicitudId);
                    fireEditingStopped();
                });

                JButton btnEliminar = new JButton("Eliminar");
                btnEliminar.setBackground(new Color(142, 68, 173));
                btnEliminar.setForeground(Color.BLACK);
                btnEliminar.setFocusPainted(false);
                btnEliminar.setMargin(new Insets(2, 8, 2, 8));
                btnEliminar.addActionListener(e -> {
                    eliminarSolicitud(solicitudId);
                    fireEditingStopped();
                });

                panel1.add(btnVer);
                panel1.add(btnEditar);
                panel1.add(btnEliminar);
                return panel1;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }

            @Override
            public boolean stopCellEditing() {
                return super.stopCellEditing();
            }
        });

        table.getColumnModel().getColumn(6).setPreferredWidth(300);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);


        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setOpaque(false);

        JButton btnAgregar = new JButton("Nueva Solicitud");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFocusPainted(false);

        actionsPanel.add(btnAgregar);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            String formularioName = "FORMULARIO_SOLICITUD";
            boolean panelExists = false;

            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof JPanel && formularioName.equals(comp.getName())) {
                    panelExists = true;
                    break;
                }
            }

            if (!panelExists) {
                JPanel formularioPanel = formularioSolicitud();
                formularioPanel.setName(formularioName);
                contentPanel.add(formularioPanel, formularioName);
            }

            cardLayout.show(contentPanel, formularioName);
        });

        return panel;
    }


    private void verSolicitud(int solicitudId) {
        try {
            Solicitud solicitud = SolicitudDAO.getById(solicitudId);

            if (solicitud != null) {
                JPanel detallePanel = new JPanel();
                detallePanel.setLayout(new BorderLayout(10, 10));
                detallePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                detallePanel.setBackground(Color.WHITE);

                JLabel lblTitle = new JLabel("Detalles de Solicitud #" + solicitudId, JLabel.CENTER);
                lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
                detallePanel.add(lblTitle, BorderLayout.NORTH);

                JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                infoPanel.setBackground(Color.WHITE);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

                addInfoRow(infoPanel, "ID:", String.valueOf(solicitud.getId()));
                addInfoRow(infoPanel, "Solicitante:", solicitud.getNombreSolicitante());
                addInfoRow(infoPanel, "Correo:", solicitud.getCorreo());
                addInfoRow(infoPanel, "Teléfono:", solicitud.getTelefono());
                addInfoRow(infoPanel, "Dirección:", solicitud.getDireccion());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String fechaSolicitud = dateFormat.format(solicitud.getFechaSolicitud());
                String fechaProgramada = dateFormat.format(solicitud.getFechaProgramada());

                addInfoRow(infoPanel, "Fecha de Solicitud:", fechaSolicitud);
                addInfoRow(infoPanel, "Fecha Programada:", fechaProgramada);
                addInfoRow(infoPanel, "Hora Programada:", solicitud.getHoraProgramada());
                addInfoRow(infoPanel, "Estado:", solicitud.getEstado().getNombre());

                String nombreTecnico = "Sin asignar";
                if (solicitud.getIdTecnico() > 0) {
                    Usuario tecnico = UsuarioDAO.findById(solicitud.getIdTecnico());
                    if (tecnico != null) {
                        nombreTecnico = tecnico.getNombre();
                    }
                }
                addInfoRow(infoPanel, "Técnico Asignado:", nombreTecnico);

                JLabel lblNotas = new JLabel("Notas:", JLabel.LEFT);
                lblNotas.setFont(new Font("Arial", Font.BOLD, 12));
                addInfoRow(infoPanel, "Notas:", solicitud.getNotas());

                JTextArea txtNotas = new JTextArea(solicitud.getNotas());
                txtNotas.setEditable(false);
                txtNotas.setLineWrap(true);
                txtNotas.setWrapStyleWord(true);
                txtNotas.setBackground(new Color(245, 245, 245));
                txtNotas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                JScrollPane scrollNotas = new JScrollPane(txtNotas);
                scrollNotas.setPreferredSize(new Dimension(400, 100));

                JPanel notasPanel = new JPanel(new BorderLayout(5, 5));
                notasPanel.setBackground(Color.WHITE);
                notasPanel.add(lblNotas, BorderLayout.NORTH);
                notasPanel.add(scrollNotas, BorderLayout.CENTER);

                detallePanel.add(infoPanel, BorderLayout.CENTER);
                detallePanel.add(notasPanel, BorderLayout.SOUTH);

                JDialog dialog = new JDialog();
                dialog.setTitle("Detalles de Solicitud");
                dialog.setModal(true);
                dialog.setSize(600, 500);
                dialog.setLocationRelativeTo(null);
                dialog.setContentPane(detallePanel);

                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.setBackground(new Color(52, 152, 219));
                btnCerrar.setForeground(Color.BLACK);
                btnCerrar.setFocusPainted(false);
                btnCerrar.addActionListener(e -> dialog.dispose());

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(Color.WHITE);
                buttonPanel.add(btnCerrar);

                detallePanel.add(buttonPanel, BorderLayout.SOUTH);

                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se encontró la solicitud con ID: " + solicitudId,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar los detalles de la solicitud: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", e);
        }
    }

    private void editarSolicitud(int solicitudId) {
        try {
            Solicitud solicitud = SolicitudDAO.getById(solicitudId);

            if (solicitud != null) {
                String formularioName = "FORMULARIO_EDITAR_SOLICITUD_" + solicitudId;
                JPanel formularioPanel;
                boolean panelExists = false;

                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof JPanel && formularioName.equals(comp.getName())) {
                        panelExists = true;
                        break;
                    }
                }

                if (!panelExists) {
                    formularioPanel = crearFormularioEdicion(solicitud);
                    formularioPanel.setName(formularioName);
                    contentPanel.add(formularioPanel, formularioName);
                }

                cardLayout.show(contentPanel, formularioName);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se encontró la solicitud con ID: " + solicitudId,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el formulario de edición: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", e);
        }
    }

    private void eliminarSolicitud(int solicitudId) {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea eliminar la solicitud #" + solicitudId + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminado = SolicitudDAO.delete(solicitudId);

                if (eliminado) {
                    JOptionPane.showMessageDialog(null,
                            "La solicitud #" + solicitudId + " ha sido eliminada correctamente.",
                            "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);

                    actualizarTablaSolicitudes();
                    actualizarEstadisticas(PANEL_SOLICITUDES);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se pudo eliminar la solicitud #" + solicitudId + ".",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al intentar eliminar la solicitud: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", e);
        }
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label, JLabel.LEFT);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblValue = new JLabel(value, JLabel.LEFT);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(lblLabel);
        panel.add(lblValue);
    }

    private JPanel crearFormularioEdicion(Solicitud solicitud) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Editar Solicitud #" + solicitud.getId(), JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 20, 0),
                BorderFactory.createTitledBorder("Detalles de la Solicitud")
        ));

        JTextField txtNombre = new JTextField(solicitud.getNombreSolicitante());
        JTextField txtCorreo = new JTextField(solicitud.getCorreo());
        JTextField txtTelefono = new JTextField(solicitud.getTelefono());
        JTextField txtDireccion = new JTextField(solicitud.getDireccion());


        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        JTextField txtFecha = new JTextField(dateFormat.format(solicitud.getFechaProgramada()));
        JTextField txtHora = new JTextField(solicitud.getHoraProgramada());

        EstadoSolicitud[] estados = EstadoSolicitud.values();
        JComboBox<EstadoSolicitud> cmbEstado = new JComboBox<>(estados);
        cmbEstado.setSelectedItem(solicitud.getEstado());

        java.util.List<Usuario> tecnicos = UsuarioDAO.getTecnicos();
        String[] tecnicosNombres = new String[tecnicos.size() + 1];
        tecnicosNombres[0] = "Sin asignar";
        for (int i = 0; i < tecnicos.size(); i++) {
            tecnicosNombres[i + 1] = tecnicos.get(i).getNombre();
        }
        JComboBox<String> cmbTecnico = new JComboBox<>(tecnicosNombres);

        if (solicitud.getIdTecnico() > 0) {
            for (int i = 0; i < tecnicos.size(); i++) {
                if (tecnicos.get(i).getId() == solicitud.getIdTecnico()) {
                    cmbTecnico.setSelectedIndex(i + 1);
                    break;
                }
            }
        }

        JTextArea txtNotas = new JTextArea(solicitud.getNotas(), 3, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);

        formPanel.add(new JLabel("Nombre del Solicitante:*"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Correo:"));
        formPanel.add(txtCorreo);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Dirección:*"));
        formPanel.add(txtDireccion);
        formPanel.add(new JLabel("Fecha Programada (" + DATE_FORMAT + "):*"));
        formPanel.add(txtFecha);
        formPanel.add(new JLabel("Hora Programada (" + TIME_FORMAT + "):*"));
        formPanel.add(txtHora);
        formPanel.add(new JLabel("Estado:"));
        formPanel.add(cmbEstado);
        formPanel.add(new JLabel("Técnico Asignado:"));
        formPanel.add(cmbTecnico);
        formPanel.add(new JLabel("Notas:"));
        formPanel.add(scrollNotas);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String fecha = txtFecha.getText().trim();
            String hora = txtHora.getText().trim();
            String notas = txtNotas.getText().trim();
            EstadoSolicitud estadoSeleccionado = (EstadoSolicitud) cmbEstado.getSelectedItem();
            int tecnicoIndex = cmbTecnico.getSelectedIndex();

            if (nombre.isEmpty() || direccion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, complete todos los campos obligatorios (*)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correo.isEmpty() && !Validations.isValidEmail(correo)) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese un correo válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SimpleDateFormat dateFormatValidator = new SimpleDateFormat(DATE_FORMAT);
            dateFormatValidator.setLenient(false);
            Date fechaProgramada;
            try {
                fechaProgramada = dateFormatValidator.parse(fecha);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una fecha válida en formato " + DATE_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Validations.isValidTime(hora)) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una hora válida en formato " + TIME_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                solicitud.setNombreSolicitante(nombre);
                solicitud.setCorreo(correo);
                solicitud.setTelefono(telefono);
                solicitud.setDireccion(direccion);
                solicitud.setFechaProgramada(fechaProgramada);
                solicitud.setHoraProgramada(hora);
                solicitud.setNotas(notas);

                solicitud.setEstado(estadoSeleccionado);

                if (tecnicoIndex > 0) {
                    solicitud.setIdTecnico(tecnicos.get(tecnicoIndex - 1).getId());

                    if (estadoSeleccionado == EstadoSolicitud.PENDIENTE) {
                        solicitud.setEstado(EstadoSolicitud.ASIGNADA);
                        JOptionPane.showMessageDialog(panel,
                                "Se ha asignado un técnico, por lo que el estado ha cambiado a 'Asignada'",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    solicitud.setIdTecnico(0);

                    if (estadoSeleccionado == EstadoSolicitud.ASIGNADA) {
                        JOptionPane.showMessageDialog(panel,
                                "Ha seleccionado el estado 'Asignada' pero no ha asignado un técnico.\n" +
                                        "Por favor, seleccione un técnico o cambie el estado.",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                boolean actualizado = SolicitudDAO.update(solicitud);

                if (actualizado) {
                    JOptionPane.showMessageDialog(panel,
                            "Solicitud actualizada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    cardLayout.show(contentPanel, PANEL_SOLICITUDES);

                    actualizarTablaSolicitudes();
                    actualizarEstadisticas(PANEL_SOLICITUDES);
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "Error al actualizar la solicitud. Por favor, inténtelo de nuevo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Error al actualizar la solicitud: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", ex);
            }
        });

        btnCancelar.addActionListener(e -> {cardLayout.show(contentPanel, PANEL_SOLICITUDES);});

        return panel;
    }


    private static final String DATE_FORMAT = "dd-MM-yy";
    private static final String TIME_FORMAT = "HH:mm";

    private JPanel formularioSolicitud() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setName("FORMULARIO_SOLICITUD");

        JLabel lblTitle = new JLabel("Formulario de Nueva Solicitud", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 20, 0),
                BorderFactory.createTitledBorder("Detalles de la Solicitud")
        ));

        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtFecha = new JTextField();
        JTextField txtHora = new JTextField();

        EstadoSolicitud[] estados = EstadoSolicitud.values();
        JComboBox<EstadoSolicitud> cmbEstado = new JComboBox<>(estados);
        cmbEstado.setSelectedItem(EstadoSolicitud.PENDIENTE);

        java.util.List<Usuario> tecnicos = UsuarioDAO.getTecnicos();
        String[] tecnicosNombres = new String[tecnicos.size() + 1];
        tecnicosNombres[0] = "Sin asignar";
        for (int i = 0; i < tecnicos.size(); i++) {
            tecnicosNombres[i + 1] = tecnicos.get(i).getNombre();
        }
        JComboBox<String> cmbTecnico = new JComboBox<>(tecnicosNombres);

        JTextArea txtNotas = new JTextArea(3, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);

        formPanel.add(new JLabel("Nombre del Solicitante:*"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Correo:"));
        formPanel.add(txtCorreo);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Dirección:*"));
        formPanel.add(txtDireccion);
        formPanel.add(new JLabel("Fecha Programada (" + DATE_FORMAT + "):*"));
        formPanel.add(txtFecha);
        formPanel.add(new JLabel("Hora Programada (" + TIME_FORMAT + "):*"));
        formPanel.add(txtHora);
        formPanel.add(new JLabel("Estado:"));
        formPanel.add(cmbEstado);
        formPanel.add(new JLabel("Técnico Asignado:"));
        formPanel.add(cmbTecnico);
        formPanel.add(new JLabel("Notas:"));
        formPanel.add(scrollNotas);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar Solicitud");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String fecha = txtFecha.getText().trim();
            String hora = txtHora.getText().trim();
            String notas = txtNotas.getText().trim();
            EstadoSolicitud estadoSeleccionado = (EstadoSolicitud) cmbEstado.getSelectedItem();
            int tecnicoIndex = cmbTecnico.getSelectedIndex();

            if (nombre.isEmpty() || direccion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, complete todos los campos obligatorios (*)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correo.isEmpty() && !Validations.isValidEmail(correo)) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese un correo válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false);
            Date fechaProgramada;
            try {
                fechaProgramada = dateFormat.parse(fecha);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una fecha válida en formato " + DATE_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Validations.isValidTime(hora)) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una hora válida en formato " + TIME_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Solicitud nuevaSolicitud = new Solicitud();
                nuevaSolicitud.setNombreSolicitante(nombre);
                nuevaSolicitud.setCorreo(correo);
                nuevaSolicitud.setTelefono(telefono);
                nuevaSolicitud.setDireccion(direccion);
                nuevaSolicitud.setFechaSolicitud(new Date());
                nuevaSolicitud.setFechaProgramada(fechaProgramada);
                nuevaSolicitud.setHoraProgramada(hora);

                nuevaSolicitud.setEstado(estadoSeleccionado);

                nuevaSolicitud.setNotas(notas);
                nuevaSolicitud.setIdUsuarioRegistro(1);

                if (tecnicoIndex > 0) {
                    nuevaSolicitud.setIdTecnico(tecnicos.get(tecnicoIndex - 1).getId());

                    if (estadoSeleccionado == EstadoSolicitud.PENDIENTE) {
                        nuevaSolicitud.setEstado(EstadoSolicitud.ASIGNADA);
                    }
                } else {
                    nuevaSolicitud.setIdTecnico(0);

                    if (estadoSeleccionado == EstadoSolicitud.ASIGNADA) {
                        JOptionPane.showMessageDialog(panel,
                                "Ha seleccionado el estado 'Asignada' pero no ha asignado un técnico.\n" +
                                        "Por favor, seleccione un técnico o cambie el estado.",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                int idSolicitud = SolicitudDAO.create(nuevaSolicitud);

                if (idSolicitud > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "Solicitud guardada exitosamente con ID: " + idSolicitud,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    cardLayout.show(contentPanel, PANEL_SOLICITUDES);

                    actualizarTablaSolicitudes();
                    actualizarEstadisticas(PANEL_SOLICITUDES);

                    txtNombre.setText("");
                    txtCorreo.setText("");
                    txtTelefono.setText("");
                    txtDireccion.setText("");
                    txtFecha.setText("");
                    txtHora.setText("");
                    txtNotas.setText("");
                    cmbEstado.setSelectedItem(EstadoSolicitud.PENDIENTE);
                    cmbTecnico.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "Error al guardar la solicitud. Por favor, inténtelo de nuevo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Error al guardar la solicitud: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", ex);
            }
        });

        btnCancelar.addActionListener(e -> {cardLayout.show(contentPanel, PANEL_SOLICITUDES);});

        return panel;
    }

    private int getComponentIndex(Container container, String name) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.getComponent(i).getName() != null &&
                    container.getComponent(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private Object[][] getUsuariosTableData() {
        java.util.List<Object[]> rows = new ArrayList<>();

        java.util.List<Usuario> allUsuarios = UsuarioDAO.findAll();

        for (Usuario usuario : allUsuarios) {
            Object[] row = new Object[7];
            row[0] = usuario.getId();
            row[1] = usuario.getUsuario();
            row[2] = usuario.getNombre();
            row[3] = usuario.getRol();
            row[4] = usuario.getUltimoAcceso();
            row[5] = usuario.isActivo() ? "Activo" : "Inactivo";
            row[6] = "";
            rows.add(row);
        }

        return rows.toArray(new Object[0][]);
    }

    private JTable usuariosTable;

    private JPanel createUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setName(PANEL_USUARIOS);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Usuarios");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Usuario: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Usuario", "Nombre", "Rol", "Último Acceso", "Estado", "Acciones"};

        DefaultTableModel model = new DefaultTableModel(getUsuariosTableData(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        usuariosTable = new JTable(model);
        usuariosTable.setRowHeight(50);
        usuariosTable.setShowVerticalLines(false);
        usuariosTable.getTableHeader().setBackground(new Color(240, 240, 240));
        usuariosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        usuariosTable.setRowSelectionAllowed(false);
        usuariosTable.setCellSelectionEnabled(false);

        usuariosTable.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 3, 8, 0));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            buttonPanel.setOpaque(false);

            JButton btnResetPassword = new JButton("Reset");
            btnResetPassword.setBackground(new Color(52, 152, 219));
            btnResetPassword.setForeground(new Color(0, 0, 0));
            btnResetPassword.setFocusPainted(false);
            btnResetPassword.setMargin(new Insets(2, 8, 2, 8));

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(new Color(0, 0, 0));
            btnEditar.setFocusPainted(false);
            btnEditar.setMargin(new Insets(2, 8, 2, 8));

            String estado = "";
            if (row < table1.getRowCount()) {
                estado = table1.getValueAt(row, 5) != null ? table1.getValueAt(row, 5).toString() : "";
            }

            String textoBoton = "Activo".equals(estado) ? "Desactivar" : "Activar";
            JButton btnToggleActive = new JButton(textoBoton);
            btnToggleActive.setBackground(new Color(142, 68, 173));
            btnToggleActive.setForeground(new Color(0, 0, 0));
            btnToggleActive.setFocusPainted(false);
            btnToggleActive.setMargin(new Insets(2, 8, 2, 8));

            buttonPanel.add(btnResetPassword);
            buttonPanel.add(btnEditar);
            buttonPanel.add(btnToggleActive);

            return buttonPanel;
        });


        usuariosTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()) {

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JPanel panel1 = new JPanel();
                panel1.setLayout(new GridLayout(1, 3, 8, 0));
                panel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                panel1.setOpaque(false);

                int usuarioId = Integer.parseInt(table.getValueAt(row, 0).toString());
                String username = table.getValueAt(row, 1).toString();
                boolean isActive = "Activo".equals(table.getValueAt(row, 5).toString());

                JButton btnResetPassword = new JButton("Reset");
                btnResetPassword.setBackground(new Color(52, 152, 219));
                btnResetPassword.setForeground(new Color(0, 0, 0));
                btnResetPassword.setFocusPainted(false);
                btnResetPassword.setMargin(new Insets(2, 8, 2, 8));
                btnResetPassword.addActionListener(e -> {
                    resetearPassword(usuarioId, username);
                    fireEditingStopped();
                });

                JButton btnEditar = new JButton("Editar");
                btnEditar.setBackground(new Color(243, 156, 18));
                btnEditar.setForeground(Color.BLACK);
                btnEditar.setFocusPainted(false);
                btnEditar.setMargin(new Insets(2, 8, 2, 8));
                btnEditar.addActionListener(e -> {
                    editarUsuario(usuarioId);
                    fireEditingStopped();
                });

                String textoBoton = isActive ? "Desactivar" : "Activar";
                JButton btnToggleActive = new JButton(textoBoton);
                btnToggleActive.setBackground(new Color(142, 68, 173));
                btnToggleActive.setForeground(Color.BLACK);
                btnToggleActive.setFocusPainted(false);
                btnToggleActive.setMargin(new Insets(2, 8, 2, 8));
                btnToggleActive.addActionListener(e -> {
                    cambiarEstadoUsuario(usuarioId, isActive);
                    fireEditingStopped();
                });

                panel1.add(btnResetPassword);
                panel1.add(btnEditar);
                panel1.add(btnToggleActive);
                return panel1;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }

            @Override
            public boolean stopCellEditing() {
                return super.stopCellEditing();
            }
        });

        usuariosTable.getColumnModel().getColumn(6).setPreferredWidth(300);
        usuariosTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        usuariosTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        usuariosTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        usuariosTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        usuariosTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        usuariosTable.getColumnModel().getColumn(5).setPreferredWidth(80);

        usuariosTable.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
            if (column == 6) {
                return usuariosTable.getColumnModel().getColumn(6).getCellRenderer().getTableCellRendererComponent(
                        table1, value, isSelected, hasFocus, row, column);
            }

            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setOpaque(true);

            if (row < table1.getRowCount() && column < table1.getColumnCount()) {
                String estado = table1.getValueAt(row, 5) != null ? (String) table1.getValueAt(row, 5) : "";
                if ("Inactivo".equals(estado)) {
                    label.setBackground(new Color(255, 236, 236));
                    label.setForeground(Color.DARK_GRAY);
                } else {
                    label.setBackground(isSelected ? table1.getSelectionBackground() : Color.WHITE);
                    label.setForeground(isSelected ? table1.getSelectionForeground() : Color.BLACK);
                }
            }

            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

            return label;
        });

        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnAgregar = new JButton("Crear Usuario");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregar.addActionListener(e -> {mostrarFormularioCrearUsuario();});

        actionsPanel.add(btnAgregar);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }


    private void mostrarFormularioCrearUsuario() {
        String formularioName = "FORMULARIO_USUARIO";
        boolean panelExists = false;

        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof JPanel && formularioName.equals(comp.getName())) {
                panelExists = true;
                break;
            }
        }

        if (!panelExists) {
            JPanel formularioPanel = crearFormularioUsuario();
            formularioPanel.setName(formularioName);
            contentPanel.add(formularioPanel, formularioName);
        }

        cardLayout.show(contentPanel, formularioName);
    }

    private JPanel crearFormularioUsuario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setName("FORMULARIO_USUARIO");

        JLabel lblTitle = new JLabel("Formulario de Nuevo Usuario", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 20, 0),
                BorderFactory.createTitledBorder("Detalles del Usuario")
        ));

        JTextField txtUsuario = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtTelefono = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();

        RolUsuario[] roles = RolUsuario.values();
        JComboBox<RolUsuario> cmbRol = new JComboBox<>(roles);
        cmbRol.setSelectedItem(RolUsuario.EMPLEADO);

        formPanel.add(new JLabel("Usuario:*"));
        formPanel.add(txtUsuario);
        formPanel.add(new JLabel("Nombre completo:*"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Correo electrónico:"));
        formPanel.add(txtCorreo);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Contraseña:*"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Confirmar contraseña:*"));
        formPanel.add(txtConfirmPassword);
        formPanel.add(new JLabel("Rol:*"));
        formPanel.add(cmbRol);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            RolUsuario rol = (RolUsuario) cmbRol.getSelectedItem();

            if (usuario.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, complete todos los campos obligatorios (*)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correo.isEmpty() && !Validations.isValidEmail(correo)) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese un correo válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(panel,
                        "Las contraseñas no coinciden",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setUsuario(usuario);
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setCorreo(correo);
                nuevoUsuario.setTelefono(telefono);
                nuevoUsuario.setContrasena(password);
                nuevoUsuario.setRol(rol);
                nuevoUsuario.setFechaRegistro(new Date());
                nuevoUsuario.setActivo(true);

                UsuarioDAO dao = new UsuarioDAO();
                boolean creado = dao.create(nuevoUsuario);

                if (creado) {
                    JOptionPane.showMessageDialog(panel,
                            "Usuario guardado exitosamente con ID: " + nuevoUsuario.getId(),
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    cardLayout.show(contentPanel, PANEL_USUARIOS);

                    actualizarTablaUsuarios();

                    txtUsuario.setText("");
                    txtNombre.setText("");
                    txtCorreo.setText("");
                    txtTelefono.setText("");
                    txtPassword.setText("");
                    txtConfirmPassword.setText("");
                    cmbRol.setSelectedItem(RolUsuario.EMPLEADO);
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "Error al guardar el usuario. Por favor, inténtelo de nuevo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Error al guardar el usuario: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", ex);
            }
        });

        btnCancelar.addActionListener(e -> {cardLayout.show(contentPanel, PANEL_USUARIOS);});

        return panel;
    }


    private void actualizarTablaUsuarios() {
        if (usuariosTable != null) {
            TableCellRenderer accionesRenderer = usuariosTable.getColumnModel().getColumn(6).getCellRenderer();
            TableCellEditor accionesEditor = usuariosTable.getColumnModel().getColumn(6).getCellEditor();

            DefaultTableModel model = (DefaultTableModel) usuariosTable.getModel();
            model.setDataVector(getUsuariosTableData(),
                    new String[]{"ID", "Usuario", "Nombre", "Rol", "Último Acceso", "Estado", "Acciones"});

            if (usuariosTable.getColumnCount() >= 7) {
                usuariosTable.getColumnModel().getColumn(6).setCellRenderer(accionesRenderer);
                usuariosTable.getColumnModel().getColumn(6).setCellEditor(accionesEditor);

                usuariosTable.getColumnModel().getColumn(6).setPreferredWidth(300);
                usuariosTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                usuariosTable.getColumnModel().getColumn(1).setPreferredWidth(100);
                usuariosTable.getColumnModel().getColumn(2).setPreferredWidth(150);
                usuariosTable.getColumnModel().getColumn(3).setPreferredWidth(100);
                usuariosTable.getColumnModel().getColumn(4).setPreferredWidth(200);
                usuariosTable.getColumnModel().getColumn(5).setPreferredWidth(80);
            }

            usuariosTable.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
                if (column == 6) {
                    return accionesRenderer.getTableCellRendererComponent(
                            table1, value, isSelected, hasFocus, row, column);
                }

                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setOpaque(true);

                if (row < table1.getRowCount() && column < table1.getColumnCount()) {
                    String estado = table1.getValueAt(row, 5) != null ? (String) table1.getValueAt(row, 5) : "";
                    if ("Inactivo".equals(estado)) {
                        label.setBackground(new Color(255, 236, 236));
                        label.setForeground(Color.DARK_GRAY);
                    } else {
                        label.setBackground(isSelected ? table1.getSelectionBackground() : Color.WHITE);
                        label.setForeground(isSelected ? table1.getSelectionForeground() : Color.BLACK);
                    }
                }

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

                return label;
            });

            usuariosTable.revalidate();
            usuariosTable.repaint();
        }
    }


    private void resetearPassword(int usuarioId, String username) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblInfo = new JLabel("Resetear contraseña para: " + username);
        JLabel lblPassword = new JLabel("Nueva contraseña:");
        JLabel lblConfirm = new JLabel("Confirmar contraseña:");

        JPasswordField txtPassword = new JPasswordField(20);
        JPasswordField txtConfirm = new JPasswordField(20);

        panel.add(lblInfo);
        panel.add(new JSeparator());
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(lblConfirm);
        panel.add(txtConfirm);

        Object originalButtonForeground = UIManager.get("Button.foreground");

        UIManager.put("Button.foreground", Color.BLACK);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Resetear Contraseña", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        UIManager.put("Button.foreground", originalButtonForeground);

        if (result == JOptionPane.OK_OPTION) {
            char[] passwordChars = txtPassword.getPassword();
            char[] confirmChars = txtConfirm.getPassword();
            String password = new String(passwordChars);
            String confirm = new String(confirmChars);

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña no puede estar vacía",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this,
                        "Las contraseñas no coinciden",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                boolean actualizado = new UsuarioDAO().updatePassword(usuarioId, password);

                if (actualizado) {
                    JOptionPane.showMessageDialog(this,
                            "Contraseña actualizada correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar la contraseña",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", ex);
            }
        }
    }

    private void editarUsuario(int usuarioId) {
        try {
            Usuario usuario = UsuarioDAO.findById(usuarioId);

            if (usuario == null) {
                Object originalButtonForeground = UIManager.get("Button.foreground");

                try {
                    UIManager.put("Button.foreground", Color.BLACK);

                    JOptionPane.showMessageDialog(this,
                            "No se encontró el usuario con ID: " + usuarioId,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    UIManager.put("Button.foreground", originalButtonForeground);
                }
                return;
            }

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel lblUsername = new JLabel("Usuario:");
            JTextField txtUsername = new JTextField(usuario.getUsuario());

            JLabel lblNombre = new JLabel("Nombre completo:");
            JTextField txtNombre = new JTextField(usuario.getNombre());

            JLabel lblCorreo = new JLabel("Correo electrónico:");
            JTextField txtCorreo = new JTextField(usuario.getCorreo());

            JLabel lblTelefono = new JLabel("Teléfono:");
            JTextField txtTelefono = new JTextField(usuario.getTelefono());

            JLabel lblRol = new JLabel("Rol:");
            JComboBox<RolUsuario> cmbRol = new JComboBox<>(RolUsuario.values());
            cmbRol.setSelectedItem(usuario.getRol());

            panel.add(lblUsername);
            panel.add(txtUsername);
            panel.add(lblNombre);
            panel.add(txtNombre);
            panel.add(lblCorreo);
            panel.add(txtCorreo);
            panel.add(lblTelefono);
            panel.add(txtTelefono);
            panel.add(lblRol);
            panel.add(cmbRol);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setBackground(new Color(46, 204, 113));
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar Usuario");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.BLACK);
            btnEliminar.setFocusPainted(false);

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setBackground(new Color(189, 195, 199));
            btnCancelar.setForeground(Color.BLACK);
            btnCancelar.setFocusPainted(false);

            buttonPanel.add(btnGuardar);
            buttonPanel.add(btnEliminar);
            buttonPanel.add(btnCancelar);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(panel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog(this, "Editar Usuario", true);
            dialog.setContentPane(mainPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);

            btnGuardar.addActionListener(e -> {
                String username = txtUsername.getText().trim();
                String nombre = txtNombre.getText().trim();
                String correo = txtCorreo.getText().trim();
                String telefono = txtTelefono.getText().trim();
                RolUsuario rol = (RolUsuario) cmbRol.getSelectedItem();

                if (username.isEmpty() || nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "El usuario y el nombre no pueden estar vacíos",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!correo.isEmpty() && !Validations.isValidEmail(correo)) {
                    JOptionPane.showMessageDialog(dialog,
                            "El formato del correo electrónico no es válido",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                usuario.setUsuario(username);
                usuario.setNombre(nombre);
                usuario.setCorreo(correo);
                usuario.setTelefono(telefono);
                usuario.setRol(rol);

                UsuarioDAO dao = new UsuarioDAO();
                boolean actualizado = dao.update(usuario);

                if (actualizado) {
                    JOptionPane.showMessageDialog(dialog,
                            "Usuario actualizado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    actualizarTablaUsuarios();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Error al actualizar el usuario",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnEliminar.addActionListener(e -> {
                int confirmacion = JOptionPane.showConfirmDialog(dialog,
                        "¿Está seguro de que desea eliminar el usuario " + usuario.getNombre() + "?\n" +
                                "Esta acción no se puede deshacer.",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    UsuarioDAO dao = new UsuarioDAO();
                    boolean eliminado = dao.delete(usuario.getId());

                    if (eliminado) {
                        JOptionPane.showMessageDialog(dialog,
                                "Usuario eliminado correctamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        actualizarTablaUsuarios();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "No se puede eliminar el usuario porque tiene solicitudes asociadas.\n" +
                                        "Considere desactivar el usuario en lugar de eliminarlo.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnCancelar.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);

        } catch (Exception e) {
            Object originalButtonForeground = UIManager.get("Button.foreground");

            try {
                UIManager.put("Button.foreground", Color.BLACK);

                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                UIManager.put("Button.foreground", originalButtonForeground);
            }
            Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", e);
        }
    }


    private void cambiarEstadoUsuario(int usuarioId, boolean currentStatus) {
        String accion = currentStatus ? "desactivar" : "activar";
        String mensaje = "¿Está seguro de que desea " + accion + " este usuario?";

        Object originalButtonForeground = UIManager.get("Button.foreground");

        try {
            UIManager.put("Button.foreground", Color.BLACK);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    mensaje,
                    "Confirmar acción",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    UsuarioDAO dao = new UsuarioDAO();
                    boolean actualizado = dao.toggleActive(usuarioId);

                    if (actualizado) {
                        String estadoNuevo = currentStatus ? "desactivado" : "activado";

                        JOptionPane.showMessageDialog(this,
                                "Usuario " + estadoNuevo + " correctamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        actualizarTablaUsuarios();
                        actualizarEstadisticas(PANEL_USUARIOS);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al cambiar el estado del usuario",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AdminMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", e);
                }
            }
        } finally {
            UIManager.put("Button.foreground", originalButtonForeground);
        }
    }


    private JPanel createClientesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setName(PANEL_CLIENTES);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("Gestión de Clientes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Cliente: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        java.util.List<String> clientesNombres = obtenerClientesUnicos();

        JPanel clientesContainer = new JPanel();
        clientesContainer.setLayout(new BorderLayout());
        clientesContainer.setBackground(Color.WHITE);

        JPanel clientesGrid = new JPanel();
        clientesGrid.setLayout(new BoxLayout(clientesGrid, BoxLayout.Y_AXIS));
        clientesGrid.setBackground(Color.WHITE);

        for (String nombreCliente : clientesNombres) {
            JPanel clientePanel = crearPanelCliente(nombreCliente);
            clientesGrid.add(clientePanel);
            clientesGrid.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(clientesGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        clientesGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        clientesContainer.add(scrollPane, BorderLayout.CENTER);

        panel.add(clientesContainer, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String busqueda = txtSearch.getText().trim().toLowerCase();
            filtrarClientes(clientesGrid, busqueda);
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String busqueda = txtSearch.getText().trim().toLowerCase();
                    filtrarClientes(clientesGrid, busqueda);
                }
            }
        });

        return panel;
    }

    private java.util.List<String> obtenerClientesUnicos() {
        java.util.List<String> clientesUnicos = new ArrayList<>();
        java.util.List<Solicitud> solicitudes = SolicitudDAO.getSolicitudes();

        java.util.Set<String> nombresUnicos = new java.util.HashSet<>();

        for (Solicitud solicitud : solicitudes) {
            String nombreCliente = solicitud.getNombreSolicitante();
            if (nombreCliente != null && !nombreCliente.isEmpty()) {
                nombresUnicos.add(nombreCliente);
            }
        }

        clientesUnicos.addAll(nombresUnicos);
        java.util.Collections.sort(clientesUnicos);

        return clientesUnicos;
    }

    private JPanel crearPanelCliente(String nombreCliente) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setName("cliente-" + nombreCliente.replaceAll("\\s+", "_").toLowerCase());

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        JLabel lblNombre = new JLabel(nombreCliente);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(lblNombre);

        int cantidadSolicitudes = contarSolicitudesPorCliente(nombreCliente);
        JLabel lblCantidad = new JLabel("Solicitudes: " + cantidadSolicitudes);
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCantidad.setForeground(new Color(100, 100, 100));
        infoPanel.add(lblCantidad);

        panel.add(infoPanel, BorderLayout.WEST);

        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        accionesPanel.setOpaque(false);

        JButton btnHistorial = new JButton("Ver Historial");
        btnHistorial.setBackground(new Color(52, 152, 219));
        btnHistorial.setForeground(Color.BLACK);
        btnHistorial.setFocusPainted(false);
        btnHistorial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorial.addActionListener(e -> verHistorialCliente(nombreCliente));

        accionesPanel.add(btnHistorial);
        panel.add(accionesPanel, BorderLayout.EAST);

        return panel;
    }


    private void verHistorialCliente(String nombreCliente) {
        java.util.List<Solicitud> solicitudesCliente = obtenerSolicitudesPorCliente(nombreCliente);

        JDialog dialog = new JDialog(this, "Historial de " + nombreCliente, true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Historial de Reparaciones: " + nombreCliente);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Fecha Solicitud", "Fecha Programada", "Dirección", "Estado", "Técnico"};

        Object[][] data = new Object[solicitudesCliente.size()][6];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < solicitudesCliente.size(); i++) {
            Solicitud s = solicitudesCliente.get(i);
            data[i][0] = s.getId();
            data[i][1] = s.getFechaSolicitud() != null ? dateFormat.format(s.getFechaSolicitud()) : "";
            data[i][2] = s.getFechaProgramada() != null ? dateFormat.format(s.getFechaProgramada()) : "";
            data[i][3] = s.getDireccion();
            data[i][4] = s.getEstado().getNombre();
            data[i][5] = s.getNombreTecnico() != null ? s.getNombreTecnico() : "Sin asignar";
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        table.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

            if (column == 4) {
                String estado = value.toString();
                switch (estado) {
                    case "Pendiente":
                        label.setBackground(new Color(253, 237, 176));
                        break;
                    case "Asignada":
                        label.setBackground(new Color(214, 234, 248));
                        break;
                    case "En Progreso":
                        label.setBackground(new Color(217, 237, 247));
                        break;
                    case "Completada":
                        label.setBackground(new Color(212, 237, 218));
                        break;
                    case "Cancelada":
                        label.setBackground(new Color(248, 215, 218));
                        break;
                    default:
                        label.setBackground(isSelected ? table1.getSelectionBackground() : Color.WHITE);
                }
            } else {
                label.setBackground(isSelected ? table1.getSelectionBackground() : Color.WHITE);
            }

            label.setForeground(isSelected ? table1.getSelectionForeground() : Color.BLACK);
            return label;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setBackground(new Color(52, 152, 219));
        btnVerDetalles.setForeground(Color.BLACK);
        btnVerDetalles.setFocusPainted(false);
        btnVerDetalles.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int solicitudId = (int) table.getValueAt(selectedRow, 0);
                dialog.dispose();
                verSolicitud(solicitudId);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, seleccione una solicitud para ver sus detalles",
                        "Selección Requerida",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(108, 117, 125));
        btnCerrar.setForeground(Color.BLACK);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnVerDetalles);
        buttonPanel.add(btnCerrar);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private java.util.List<Solicitud> obtenerSolicitudesPorCliente(String nombreCliente) {
        java.util.List<Solicitud> solicitudesCliente = new ArrayList<>();
        java.util.List<Solicitud> todasSolicitudes = SolicitudDAO.getSolicitudes();

        for (Solicitud solicitud : todasSolicitudes) {
            if (nombreCliente.equals(solicitud.getNombreSolicitante())) {
                solicitudesCliente.add(solicitud);
            }
        }

        solicitudesCliente.sort((s1, s2) -> {
            if (s1.getFechaSolicitud() == null) return 1;
            if (s2.getFechaSolicitud() == null) return -1;
            return s2.getFechaSolicitud().compareTo(s1.getFechaSolicitud());
        });

        return solicitudesCliente;
    }

    private int contarSolicitudesPorCliente(String nombreCliente) {
        int contador = 0;
        java.util.List<Solicitud> solicitudes = SolicitudDAO.getSolicitudes();

        for (Solicitud solicitud : solicitudes) {
            if (nombreCliente.equals(solicitud.getNombreSolicitante())) {
                contador++;
            }
        }

        return contador;
    }

    private void filtrarClientes(JPanel clientesGrid, String busqueda) {
        for (Component comp : clientesGrid.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel clientePanel = (JPanel) comp;
                String nombrePanel = clientePanel.getName();

                if (nombrePanel != null && nombrePanel.startsWith("cliente-")) {
                    for (Component child : clientePanel.getComponents()) {
                        if (child instanceof JPanel) {
                            JPanel infoPanel = (JPanel) child;
                            for (Component infoChild : infoPanel.getComponents()) {
                                if (infoChild instanceof JLabel) {
                                    JLabel lblNombre = (JLabel) infoChild;
                                    String nombreCliente = lblNombre.getText().toLowerCase();

                                    boolean coincide = busqueda.isEmpty() || nombreCliente.contains(busqueda);
                                    clientePanel.setVisible(coincide);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        clientesGrid.revalidate();
        clientesGrid.repaint();
    }
}


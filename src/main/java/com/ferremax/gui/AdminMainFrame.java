package com.ferremax.gui;

import com.ferremax.controller.LoginController;
import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.Solicitud;
import com.ferremax.model.Usuario;
import com.ferremax.dao.*;

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

public class AdminMainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private static final String PANEL_INICIO = "INICIO";
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
        contentPanel.add(createSolicitudesPanel(), PANEL_SOLICITUDES);
        contentPanel.add(createEmpleadosPanel(), PANEL_EMPLEADOS);
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
        addMenuItem(panel, "Gestión de Solicitudes", PANEL_SOLICITUDES, "Solicitudes");
        addMenuItem(panel, "Gestión de Empleados", PANEL_EMPLEADOS, "Empleados");
        addMenuItem(panel, "Gestión de Horarios", PANEL_HORARIOS, "Horarios");
        addMenuItem(panel, "Gestión de Usuarios", PANEL_USUARIOS, "Usuarios");

        panel.add(Box.createVerticalGlue());

        // Botón de cerrar sesión
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

        //Cerrar sesion btn
        btnLogout.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                LoginController.logout();
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        return panel;
    }

    private void addMenuItem(JPanel panel, String title, String panelName, String iconName) {
        JPanel menuItem = new JPanel(new BorderLayout());
        menuItem.setBackground(new Color(33, 33, 33));
        menuItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(Color.WHITE); // Cambiado a blanco
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
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 123, 255)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel("Panel de Administración");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitle, BorderLayout.WEST);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfoPanel.setOpaque(false);

        // Botón de actualización
        JButton btnRefresh = new JButton("Actualizar Datos");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> actualizarTodasLasTablas());
        userInfoPanel.add(btnRefresh);

        JLabel lblUsername = new JLabel("Admin");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfoPanel.add(lblUsername);

        panel.add(userInfoPanel, BorderLayout.EAST);

        return panel;
    }

    // Método para actualizar todas las tablas
    private void actualizarTodasLasTablas() {
        actualizarTablaSolicitudes();
        actualizarTablaEmpleados();
        actualizarTablaUsuarios();
        actualizarEstadisticas();

        JOptionPane.showMessageDialog(this,
                "Todas las tablas han sido actualizadas correctamente",
                "Actualización Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTablaSolicitudes() {
        int index = getComponentIndex(contentPanel, PANEL_SOLICITUDES);
        if (index != -1) {
            JPanel panelSolicitudes = (JPanel) contentPanel.getComponent(index);
            for (Component comp : panelSolicitudes.getComponents()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) comp;
                    if (scrollPane.getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) scrollPane.getViewport().getView();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();

                        // Guardar los renderizadores y editores actuales de la columna de acciones
                        TableCellRenderer accionesRenderer = table.getColumnModel().getColumn(6).getCellRenderer();
                        TableCellEditor accionesEditor = table.getColumnModel().getColumn(6).getCellEditor();

                        // Actualizar los datos
                        model.setDataVector(getSolicitudesTableData(),
                                new String[]{"ID", "Solicitante", "Contacto", "Dirección", "Fecha", "Estado", "Acciones"});

                        // Restaurar los renderizadores y editores para la columna de acciones
                        if (accionesRenderer != null) {
                            table.getColumnModel().getColumn(6).setCellRenderer(accionesRenderer);
                        }
                        if (accionesEditor != null) {
                            table.getColumnModel().getColumn(6).setCellEditor(accionesEditor);
                        }

                        // Opcional: ajustar anchos de columna si es necesario
                        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
                        table.getColumnModel().getColumn(6).setPreferredWidth(250); // Acciones
                    }
                    break;
                }
            }
        }
    }


    private void actualizarTablaEmpleados() {
        int index = getComponentIndex(contentPanel, PANEL_EMPLEADOS);
        if (index != -1) {
            JPanel panelEmpleados = (JPanel) contentPanel.getComponent(index);
            for (Component comp : panelEmpleados.getComponents()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) comp;
                    if (scrollPane.getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) scrollPane.getViewport().getView();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();

                        // Guardar el renderizador y editor de la columna de acciones
                        TableCellRenderer accionesRenderer = table.getColumnModel().getColumn(5).getCellRenderer();

                        // Actualizar los datos
                        model.setDataVector(getEmpleadosTableData(),
                                new String[]{"ID", "Nombre", "Correo", "Teléfono", "Rol", "Acciones"});

                        // Restaurar el renderizador para la columna de acciones
                        if (accionesRenderer != null) {
                            table.getColumnModel().getColumn(5).setCellRenderer(accionesRenderer);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void actualizarTablaUsuarios() {
        int index = getComponentIndex(contentPanel, PANEL_USUARIOS);
        if (index != -1) {
            JPanel panelUsuarios = (JPanel) contentPanel.getComponent(index);
            for (Component comp : panelUsuarios.getComponents()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) comp;
                    if (scrollPane.getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) scrollPane.getViewport().getView();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.setDataVector(getUsuariosTableData(),
                                new String[]{"ID", "Usuario", "Nombre", "Rol", "Último Acceso", "Estado", "Acciones"});
                    }
                    break;
                }
            }
        }
    }

    private void actualizarEstadisticas() {
        int index = getComponentIndex(contentPanel, PANEL_INICIO);
        if (index != -1) {
            // Recrear el panel de inicio con datos actualizados
            JPanel nuevoPanel = createHomePanel();
            contentPanel.remove(index);
            contentPanel.add(nuevoPanel, PANEL_INICIO, index);
            // Si el panel de inicio está actualmente visible, mostrarlo de nuevo
            if (cardLayout.toString().contains(PANEL_INICIO)) {
                cardLayout.show(contentPanel, PANEL_INICIO);
            }
        }
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

        String solicitudesP = String.valueOf(UsuarioDAO.getSolicitudesP());
        String clientesA = String.valueOf(UsuarioDAO.getEmpleadosA());
        String reparacionesReg = String .valueOf(UsuarioDAO.getReparacionesReg());
        String reparacionesR = String.valueOf(UsuarioDAO.getReparacionesR());

        dashboardPanel.add(createStatCard("Empleados Activos", clientesA, new Color(231, 76, 60)));
        dashboardPanel.add(createStatCard("Solicitudes Pendientes", solicitudesP, new Color(52, 152, 219)));
        dashboardPanel.add(createStatCard("Reparaciones Realizadas", reparacionesR, new Color(46, 204, 113)));
        dashboardPanel.add(createStatCard("Reparaciones Registradas", reparacionesReg, new Color(155, 89, 182)));

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

    //Llenar un array, list, u cualquier otro para luego poder mostrar los valores en la tabla
    private Object[][] getSolicitudesTableData() {
        java.util.List<Object[]> rows = new ArrayList<>();
        java.util.List<Solicitud> allSolicitudes = SolicitudDAO.getSolicitudes();

        for (Solicitud solicitud : allSolicitudes) {
            Object[] row = new Object[7]; // 7 columnas según tu modelo
            row[0] = solicitud.getId();
            row[1] = solicitud.getSolicitante();
            row[2] = solicitud.getContacto();
            row[3] = solicitud.getDireccion();
            row[4] = solicitud.getFecha();
            row[5] = solicitud.getEstado();
            row[6] = ""; // Columna para acciones
            rows.add(row);
        }

        return rows.toArray(new Object[0][]);
    }

    private JPanel createSolicitudesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setName(PANEL_SOLICITUDES); // Asignar un nombre al panel para identificarlo

        // Encabezado con título y buscador
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Solicitudes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabla de solicitudes
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

        // Renderizador para botones en la columna de acciones
        table.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel1.setOpaque(false);

            JButton btnVer = new JButton("Ver");
            btnVer.setBackground(new Color(52, 152, 219));
            btnVer.setForeground(Color.BLACK);
            btnVer.setFocusPainted(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.BLACK);
            btnEditar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.BLACK);
            btnEliminar.setFocusPainted(false);

            panel1.add(btnVer);
            panel1.add(btnEditar);
            panel1.add(btnEliminar);
            return panel1;
        });

        // Editor para botones en la columna de acciones
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel1.setOpaque(false);

                // Obtener el ID de la solicitud de la primera columna
                int solicitudId = Integer.parseInt(table.getValueAt(row, 0).toString());

                JButton btnVer = new JButton("Ver");
                btnVer.setBackground(new Color(52, 152, 219));
                btnVer.setForeground(Color.BLACK);
                btnVer.setFocusPainted(false);
                btnVer.addActionListener(e -> {
                    verSolicitud(solicitudId);
                    fireEditingStopped();
                });

                JButton btnEditar = new JButton("Editar");
                btnEditar.setBackground(new Color(243, 156, 18));
                btnEditar.setForeground(Color.BLACK);
                btnEditar.setFocusPainted(false);
                btnEditar.addActionListener(e -> {
                    editarSolicitud(solicitudId);
                    fireEditingStopped();
                });

                JButton btnEliminar = new JButton("Eliminar");
                btnEliminar.setBackground(new Color(231, 76, 60));
                btnEliminar.setForeground(Color.BLACK);
                btnEliminar.setFocusPainted(false);
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
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setOpaque(false);

        JButton btnAgregar = new JButton("Nueva Solicitud");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFocusPainted(false);

        actionsPanel.add(btnAgregar);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        // Botón para agregar una nueva solicitud - CORREGIDO
        btnAgregar.addActionListener(e -> {
            // Verificar si el panel de formulario ya existe
            String formularioName = "FORMULARIO_SOLICITUD";
            boolean panelExists = false;

            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof JPanel && formularioName.equals(comp.getName())) {
                    panelExists = true;
                    break;
                }
            }

            // Si no existe, crearlo y añadirlo
            if (!panelExists) {
                JPanel formularioPanel = formularioSolicitud();
                formularioPanel.setName(formularioName);
                contentPanel.add(formularioPanel, formularioName);
            }

            // Mostrar el panel de formulario
            cardLayout.show(contentPanel, formularioName);
        });

        return panel;
    }

// Métodos para manejar las acciones de los botones

    private void verSolicitud(int solicitudId) {
        try {
            // Obtener la solicitud de la base de datos
            Solicitud solicitud = SolicitudDAO.getById(solicitudId);

            if (solicitud != null) {
                // Crear un panel para mostrar los detalles
                JPanel detallePanel = new JPanel();
                detallePanel.setLayout(new BorderLayout(10, 10));
                detallePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                detallePanel.setBackground(Color.WHITE);

                // Título
                JLabel lblTitle = new JLabel("Detalles de Solicitud #" + solicitudId, JLabel.CENTER);
                lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
                detallePanel.add(lblTitle, BorderLayout.NORTH);

                // Panel con detalles
                JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                infoPanel.setBackground(Color.WHITE);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

                // Añadir información
                addInfoRow(infoPanel, "ID:", String.valueOf(solicitud.getId()));
                addInfoRow(infoPanel, "Solicitante:", solicitud.getNombreSolicitante());
                addInfoRow(infoPanel, "Correo:", solicitud.getCorreo());
                addInfoRow(infoPanel, "Teléfono:", solicitud.getTelefono());
                addInfoRow(infoPanel, "Dirección:", solicitud.getDireccion());

                // Formatear fecha
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String fechaSolicitud = dateFormat.format(solicitud.getFechaSolicitud());
                String fechaProgramada = dateFormat.format(solicitud.getFechaProgramada());

                addInfoRow(infoPanel, "Fecha de Solicitud:", fechaSolicitud);
                addInfoRow(infoPanel, "Fecha Programada:", fechaProgramada);
                addInfoRow(infoPanel, "Hora Programada:", solicitud.getHoraProgramada());
                addInfoRow(infoPanel, "Estado:", solicitud.getEstado().getNombre());

                // Obtener nombre del técnico si está asignado
                String nombreTecnico = "Sin asignar";
                if (solicitud.getIdTecnico() > 0) {
                    Usuario tecnico = UsuarioDAO.findById(solicitud.getIdTecnico());
                    if (tecnico != null) {
                        nombreTecnico = tecnico.getNombre();
                    }
                }
                addInfoRow(infoPanel, "Técnico Asignado:", nombreTecnico);

                // Notas
                JLabel lblNotas = new JLabel("Notas:", JLabel.LEFT);
                lblNotas.setFont(new Font("Arial", Font.BOLD, 12));

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

                // Mostrar en un diálogo
                JDialog dialog = new JDialog();
                dialog.setTitle("Detalles de Solicitud");
                dialog.setModal(true);
                dialog.setSize(600, 500);
                dialog.setLocationRelativeTo(null);
                dialog.setContentPane(detallePanel);

                // Botón para cerrar
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
            e.printStackTrace();
        }
    }

    private void editarSolicitud(int solicitudId) {
        try {
            // Obtener la solicitud de la base de datos
            Solicitud solicitud = SolicitudDAO.getById(solicitudId);

            if (solicitud != null) {
                // Crear o reutilizar el panel de formulario
                String formularioName = "FORMULARIO_EDITAR_SOLICITUD_" + solicitudId;
                JPanel formularioPanel = null;
                boolean panelExists = false;

                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof JPanel && formularioName.equals(comp.getName())) {
                        formularioPanel = (JPanel) comp;
                        panelExists = true;
                        break;
                    }
                }

                if (!panelExists) {
                    formularioPanel = crearFormularioEdicion(solicitud);
                    formularioPanel.setName(formularioName);
                    contentPanel.add(formularioPanel, formularioName);
                }

                // Mostrar el panel de edición
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
            e.printStackTrace();
        }
    }

    private void eliminarSolicitud(int solicitudId) {
        try {
            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea eliminar la solicitud #" + solicitudId + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Intentar eliminar la solicitud
                boolean eliminado = SolicitudDAO.delete(solicitudId);

                if (eliminado) {
                    JOptionPane.showMessageDialog(null,
                            "La solicitud #" + solicitudId + " ha sido eliminada correctamente.",
                            "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar la tabla de solicitudes
                    actualizarTablaSolicitudes();
                    actualizarEstadisticas(); // Actualizar estadísticas del dashboard
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
            e.printStackTrace();
        }
    }

    // Método auxiliar para crear filas de información en el panel de detalles
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label, JLabel.LEFT);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblValue = new JLabel(value, JLabel.LEFT);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(lblLabel);
        panel.add(lblValue);
    }

    // Método para crear el formulario de edición de una solicitud existente
    private JPanel crearFormularioEdicion(Solicitud solicitud) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título centrado en la parte superior
        JLabel lblTitle = new JLabel("Editar Solicitud #" + solicitud.getId(), JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Panel central con los campos del formulario
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 20, 0),
                BorderFactory.createTitledBorder("Detalles de la Solicitud")
        ));

        // Campos del formulario con valores preexistentes
        JTextField txtNombre = new JTextField(solicitud.getNombreSolicitante());
        JTextField txtCorreo = new JTextField(solicitud.getCorreo());
        JTextField txtTelefono = new JTextField(solicitud.getTelefono());
        JTextField txtDireccion = new JTextField(solicitud.getDireccion());

        // Formatear fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        JTextField txtFecha = new JTextField(dateFormat.format(solicitud.getFechaProgramada()));
        JTextField txtHora = new JTextField(solicitud.getHoraProgramada());

        // Selector de estado usando el enum EstadoSolicitud
        EstadoSolicitud[] estados = EstadoSolicitud.values();
        JComboBox<EstadoSolicitud> cmbEstado = new JComboBox<>(estados);
        cmbEstado.setSelectedItem(solicitud.getEstado());

        // Selector de técnico
        java.util.List<Usuario> tecnicos = UsuarioDAO.getTecnicos();
        String[] tecnicosNombres = new String[tecnicos.size() + 1];
        tecnicosNombres[0] = "Sin asignar";
        for (int i = 0; i < tecnicos.size(); i++) {
            tecnicosNombres[i + 1] = tecnicos.get(i).getNombre();
        }
        JComboBox<String> cmbTecnico = new JComboBox<>(tecnicosNombres);

        // Seleccionar el técnico actual si existe
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

        // Añadir campos al panel del formulario
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

        // Panel de botones en la parte inferior
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

        // Guardar cambios en la solicitud
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

            // Validar campos obligatorios
            if (nombre.isEmpty() || direccion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, complete todos los campos obligatorios (*)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de correo si se ha ingresado
            if (!correo.isEmpty() && !correo.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese un correo válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de fecha
            SimpleDateFormat dateFormatValidator = new SimpleDateFormat(DATE_FORMAT);
            dateFormatValidator.setLenient(false);
            Date fechaProgramada = null;
            try {
                fechaProgramada = dateFormatValidator.parse(fecha);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una fecha válida en formato " + DATE_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de hora
            if (!hora.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una hora válida en formato " + TIME_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Actualizar el objeto Solicitud con los nuevos valores
                solicitud.setNombreSolicitante(nombre);
                solicitud.setCorreo(correo);
                solicitud.setTelefono(telefono);
                solicitud.setDireccion(direccion);
                solicitud.setFechaProgramada(fechaProgramada);
                solicitud.setHoraProgramada(hora);
                solicitud.setNotas(notas);

                // Asignar el estado seleccionado
                solicitud.setEstado(estadoSeleccionado);

                // Asignar técnico si se seleccionó alguno
                if (tecnicoIndex > 0) {
                    solicitud.setIdTecnico(tecnicos.get(tecnicoIndex - 1).getId());

                    // Si se asigna un técnico y el estado es PENDIENTE, cambiar automáticamente a ASIGNADA
                    if (estadoSeleccionado == EstadoSolicitud.PENDIENTE) {
                        solicitud.setEstado(EstadoSolicitud.ASIGNADA);
                        JOptionPane.showMessageDialog(panel,
                                "Se ha asignado un técnico, por lo que el estado ha cambiado a 'Asignada'",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    solicitud.setIdTecnico(0); // Sin técnico asignado

                    // Si no hay técnico asignado y el estado es ASIGNADA, mostrar advertencia
                    if (estadoSeleccionado == EstadoSolicitud.ASIGNADA) {
                        JOptionPane.showMessageDialog(panel,
                                "Ha seleccionado el estado 'Asignada' pero no ha asignado un técnico.\n" +
                                        "Por favor, seleccione un técnico o cambie el estado.",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                // Actualizar la solicitud en la base de datos
                boolean actualizado = SolicitudDAO.update(solicitud);

                if (actualizado) {
                    JOptionPane.showMessageDialog(panel,
                            "Solicitud actualizada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Volver al panel de solicitudes
                    cardLayout.show(contentPanel, PANEL_SOLICITUDES);

                    // Actualizar todas las tablas relevantes
                    actualizarTablaSolicitudes();
                    actualizarEstadisticas(); // Actualizar estadísticas del dashboard
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "Error al actualizar la solicitud. Por favor, inténtelo de nuevo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Error al actualizar la solicitud: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Cancelar y volver al panel de solicitudes
        btnCancelar.addActionListener(e -> {
            cardLayout.show(contentPanel, PANEL_SOLICITUDES);
        });

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

        // Título centrado en la parte superior
        JLabel lblTitle = new JLabel("Formulario de Nueva Solicitud", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Panel central con los campos del formulario
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 20, 0),
                BorderFactory.createTitledBorder("Detalles de la Solicitud")
        ));

        // Campos del formulario
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtFecha = new JTextField();
        JTextField txtHora = new JTextField();

        // Selector de estado usando el enum EstadoSolicitud
        EstadoSolicitud[] estados = EstadoSolicitud.values();
        JComboBox<EstadoSolicitud> cmbEstado = new JComboBox<>(estados);
        cmbEstado.setSelectedItem(EstadoSolicitud.PENDIENTE); // Por defecto "Pendiente"

        // Selector de técnico
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

        // Añadir campos al panel del formulario
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

        // Panel de botones en la parte inferior
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

        // Guardar solicitud
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

            // Validar campos obligatorios
            if (nombre.isEmpty() || direccion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, complete todos los campos obligatorios (*)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de correo si se ha ingresado
            if (!correo.isEmpty() && !correo.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese un correo válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false);
            Date fechaProgramada = null;
            try {
                fechaProgramada = dateFormat.parse(fecha);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una fecha válida en formato " + DATE_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de hora
            if (!hora.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor, ingrese una hora válida en formato " + TIME_FORMAT,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Crear objeto Solicitud con todos los campos requeridos por SolicitudDAO.create()
                Solicitud nuevaSolicitud = new Solicitud();
                nuevaSolicitud.setNombreSolicitante(nombre);
                nuevaSolicitud.setCorreo(correo);
                nuevaSolicitud.setTelefono(telefono);
                nuevaSolicitud.setDireccion(direccion);
                nuevaSolicitud.setFechaSolicitud(new Date()); // Fecha actual
                nuevaSolicitud.setFechaProgramada(fechaProgramada);
                nuevaSolicitud.setHoraProgramada(hora);

                // Asignar el estado seleccionado
                nuevaSolicitud.setEstado(estadoSeleccionado);

                nuevaSolicitud.setNotas(notas);
                nuevaSolicitud.setIdUsuarioRegistro(1); // ID del usuario actual (admin)

                // Asignar técnico si se seleccionó alguno
                if (tecnicoIndex > 0) {
                    nuevaSolicitud.setIdTecnico(tecnicos.get(tecnicoIndex - 1).getId());

                    // Si se asigna un técnico y el estado es PENDIENTE, cambiar automáticamente a ASIGNADA
                    if (estadoSeleccionado == EstadoSolicitud.PENDIENTE) {
                        nuevaSolicitud.setEstado(EstadoSolicitud.ASIGNADA);
                    }
                } else {
                    nuevaSolicitud.setIdTecnico(0); // Sin técnico asignado

                    // Si no hay técnico asignado y el estado es ASIGNADA, mostrar advertencia
                    if (estadoSeleccionado == EstadoSolicitud.ASIGNADA) {
                        JOptionPane.showMessageDialog(panel,
                                "Ha seleccionado el estado 'Asignada' pero no ha asignado un técnico.\n" +
                                        "Por favor, seleccione un técnico o cambie el estado.",
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                // Guardar la solicitud
                int idSolicitud = SolicitudDAO.create(nuevaSolicitud);

                if (idSolicitud > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "Solicitud guardada exitosamente con ID: " + idSolicitud,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Volver al panel de solicitudes
                    cardLayout.show(contentPanel, PANEL_SOLICITUDES);

                    // Actualizar todas las tablas relevantes
                    actualizarTablaSolicitudes();
                    actualizarEstadisticas(); // Actualizar estadísticas del dashboard

                    // Limpiar el formulario para futuras entradas
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
                ex.printStackTrace();
            }
        });

        // Cancelar y volver al panel de solicitudes
        btnCancelar.addActionListener(e -> {
            cardLayout.show(contentPanel, PANEL_SOLICITUDES);
        });

        return panel;
    }

    // Método auxiliar para encontrar el índice de un componente por nombre
    private int getComponentIndex(Container container, String name) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.getComponent(i).getName() != null &&
                    container.getComponent(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }


    private Object[][] getEmpleadosTableData() {
        java.util.List<Object[]> rows = new ArrayList<>();
        java.util.List<Usuario> allEmpleados = UsuarioDAO.getEmpleados();

        for (Usuario usuario : allEmpleados) {
            Object[] row = new Object[6];
            row[0] = usuario.getId();
            row[1] = usuario.getNombre();
            row[2] = usuario.getCorreo();
            row[3] = usuario.getTelefono();
            row[4] = usuario.getRol();
            row[5] = ""; // Columna para acciones
            rows.add(row);
        }

        return rows.toArray(new Object[0][]);
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
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFocusPainted(false);

        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con tabla de empleados
        String[] columnNames = {"ID", "Nombre", "Correo", "Teléfono", "Rol", "Acciones"};;

        DefaultTableModel model = new DefaultTableModel(getEmpleadosTableData(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
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
            btnDetalles.setForeground(Color.BLACK);
            btnDetalles.setFocusPainted(false);

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.BLACK);
            btnEditar.setFocusPainted(false);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.BLACK);
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
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setBackground(new Color(52, 152, 219));
        btnGenerarReporte.setForeground(Color.BLACK);
        btnGenerarReporte.setFocusPainted(false);
        btnGenerarReporte.setFont(new Font("Arial", Font.PLAIN, 14));

        actionsPanel.add(btnAgregar);
        actionsPanel.add(btnGenerarReporte);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
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
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            JButton btnResetPassword = new JButton("Reset");
            btnResetPassword.setBackground(new Color(52, 152, 219));
            btnResetPassword.setForeground(Color.BLACK);
            btnResetPassword.setFocusPainted(false);
            btnResetPassword.setToolTipText("Resetear Contraseña");

            JButton btnEditar = new JButton("Editar");
            btnEditar.setBackground(new Color(243, 156, 18));
            btnEditar.setForeground(Color.BLACK);
            btnEditar.setFocusPainted(false);

            JButton btnToggleActive = new JButton("Activar/Desactivar");
            btnToggleActive.setBackground(new Color(142, 68, 173));
            btnToggleActive.setForeground(Color.BLACK);
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
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnPermisos = new JButton("Gestionar Permisos");
        btnPermisos.setBackground(new Color(52, 73, 94));
        btnPermisos.setForeground(Color.BLACK);
        btnPermisos.setFocusPainted(false);
        btnPermisos.setFont(new Font("Arial", Font.PLAIN, 14));

        actionsPanel.add(btnAgregar);
        actionsPanel.add(btnPermisos);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }
}


package com.ferremax.gui;

import com.ferremax.controller.LoginController;
import com.ferremax.dao.SolicitudDAO;
import com.ferremax.dao.UsuarioDAO;
import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.Solicitud;
import com.ferremax.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeMainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private static final String PANEL_INICIO = "INICIO";
    private static final String PANEL_SOLICITUDES = "SOLICITUDES";
    private static final String PANEL_CLIENTES = "CLIENTES";
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
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 246, 250));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 246, 250));

        contentPanel.add(createHomePanel(), PANEL_INICIO);
        contentPanel.add(createSolicitudesPanel(), PANEL_SOLICITUDES);
        contentPanel.add(createHorariosPanel(), PANEL_CLIENTES);
        contentPanel.add(createCredencialesPanel(), PANEL_CREDENCIALES);

        cardLayout.show(contentPanel, PANEL_INICIO);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 123, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblLogo = new JLabel("FERREMAX");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);
        panel.add(lblLogo, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        userPanel.add(btnLogout);

        panel.add(userPanel, BorderLayout.EAST);
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

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 58, 64));
        panel.setPreferredSize(new Dimension(220, getHeight()));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        addMenuItem(panel, "Inicio", PANEL_INICIO, "home");
        addMenuItem(panel, "Gestión de Solicitudes", PANEL_SOLICITUDES, "file-text");
        addMenuItem(panel, "Gestión de Clientes", PANEL_CLIENTES, "calendar");
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

                JButton btnReclamar = new JButton("Reclamar");
                btnReclamar.setBackground(new Color(52, 152, 219));
                btnReclamar.setForeground(Color.BLACK);
                btnReclamar.setFocusPainted(false);

                btnReclamar.addActionListener(e -> {
                    int confirmacion = JOptionPane.showConfirmDialog(null,
                            "¿Está seguro de que desea reclamar esta solicitud?",
                            "Confirmar Reclamo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirmacion == JOptionPane.YES_OPTION) {

                        dialog.dispose();
                    }
                });

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(Color.WHITE);
                buttonPanel.add(btnReclamar);

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

            if (!correo.isEmpty() && !correo.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
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

            if (!hora.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
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

        btnCancelar.addActionListener(e -> cardLayout.show(contentPanel, PANEL_SOLICITUDES));

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

            if (!correo.isEmpty() && !correo.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
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

            if (!hora.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
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

        btnCancelar.addActionListener(e -> cardLayout.show(contentPanel, PANEL_SOLICITUDES));

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

    private JPanel createHorariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Gestión de Horarios");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

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

        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(Color.WHITE);

        JPanel weekView = new JPanel(new GridLayout(0, 5, 5, 5));
        weekView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        weekView.setBackground(Color.WHITE);

        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        for (String day : days) {
            JLabel lblDay = new JLabel(day, JLabel.CENTER);
            lblDay.setFont(new Font("Arial", Font.BOLD, 14));
            lblDay.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
            lblDay.setPreferredSize(new Dimension(150, 40));
            weekView.add(lblDay);
        }

        String[] dates = {"05/05", "06/05", "07/05", "08/05", "09/05"};
        for (String date : dates) {
            JLabel lblDate = new JLabel(date, JLabel.CENTER);
            lblDate.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
            weekView.add(lblDate);
        }

        String[] timeSlots = {"9:00-11:00", "11:30-13:30", "14:00-16:00", "16:30-18:30"};

        for (String timeSlot : timeSlots) {
            for (int i = 0; i < 5; i++) {
                JPanel slotPanel = new JPanel(new BorderLayout());
                slotPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

                if (i == 0 && timeSlot.equals("9:00-11:00") ||
                        i == 0 && timeSlot.equals("11:30-13:30") ||
                        i == 2 && timeSlot.equals("9:00-11:00")) {
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

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

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

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("(*) Campo obligatorio para confirmar cambios");
        lblInfo.setForeground(new Color(108, 117, 125));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(lblInfo, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(contentPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
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
}
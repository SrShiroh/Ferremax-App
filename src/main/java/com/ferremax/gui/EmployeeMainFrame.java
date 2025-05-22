package com.ferremax.gui;

import com.ferremax.controller.LoginController;
import com.ferremax.dao.SolicitudDAO;
import com.ferremax.dao.UsuarioDAO;
import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Solicitud;
import com.ferremax.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
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
        contentPanel.add(createClientesPanel(), PANEL_CLIENTES);
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

        statsPanel.add(createStatCard("Solicitudes Pendientes", String.valueOf(SolicitudDAO.getSolicitudesP()), new Color(0, 123, 255)));
        statsPanel.add(createStatCard("Reparaciones Realizadas", String.valueOf(SolicitudDAO.getReparacionesR()), new Color(40, 167, 69)));

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
        List<Object[]> rows = new ArrayList<>();

        List<Solicitud> allSolicitudes = SolicitudDAO.getSolicitudes();

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
                String fechaSolicitud = solicitud.getFechaSolicitud() != null ? dateFormat.format(solicitud.getFechaSolicitud()) : "N/A";
                String fechaProgramada = solicitud.getFechaProgramada() != null ? dateFormat.format(solicitud.getFechaProgramada()) : "N/A";

                addInfoRow(infoPanel, "Fecha de Solicitud:", fechaSolicitud);
                addInfoRow(infoPanel, "Fecha Programada:", fechaProgramada);
                addInfoRow(infoPanel, "Hora Programada:", solicitud.getHoraProgramada() != null ? solicitud.getHoraProgramada() : "N/A");
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

                JTextArea txtNotas = new JTextArea(solicitud.getNotas() != null ? solicitud.getNotas() : "");
                txtNotas.setEditable(false);
                txtNotas.setLineWrap(true);
                txtNotas.setWrapStyleWord(true);
                txtNotas.setBackground(new Color(245, 245, 245));
                txtNotas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                JScrollPane scrollNotas = new JScrollPane(txtNotas);
                scrollNotas.setPreferredSize(new Dimension(400, 100));

                JPanel notasOuterPanel = new JPanel(new BorderLayout(5,5));
                notasOuterPanel.setBackground(Color.WHITE);
                notasOuterPanel.add(lblNotas, BorderLayout.NORTH);
                notasOuterPanel.add(scrollNotas, BorderLayout.CENTER);


                JPanel centerContentPanel = new JPanel(new BorderLayout(10,10));
                centerContentPanel.setBackground(Color.WHITE);
                centerContentPanel.add(infoPanel, BorderLayout.CENTER);
                centerContentPanel.add(notasOuterPanel, BorderLayout.SOUTH);


                detallePanel.add(centerContentPanel, BorderLayout.CENTER);


                JDialog dialog = new JDialog();
                dialog.setTitle("Detalles de Solicitud");
                dialog.setModal(true);
                dialog.setSize(600, 600);
                dialog.setLocationRelativeTo(null);
                dialog.setContentPane(detallePanel);

                JButton actionButton;
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(Color.WHITE);

                if (LoginController.getUsuarioLogueado().getRol() == RolUsuario.TECNICO) {
                    if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE) {
                        actionButton = new JButton("Reclamar");
                        actionButton.setBackground(new Color(52, 152, 219));
                        actionButton.setForeground(Color.BLACK);
                        actionButton.setFocusPainted(false);
                        actionButton.addActionListener(e -> {
                            int confirmacion = JOptionPane.showConfirmDialog(dialog,
                                    "¿Está seguro de que desea reclamar esta solicitud?",
                                    "Confirmar Reclamo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                SolicitudDAO.asignarTecnico(solicitud.getId(), LoginController.getUsuarioLogueado().getId());
                                solicitud.setIdTecnico(LoginController.getUsuarioLogueado().getId());
                                solicitud.setEstado(EstadoSolicitud.ASIGNADA);
                                SolicitudDAO.update(solicitud);
                                JOptionPane.showMessageDialog(dialog, "Solicitud reclamada y asignada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                actualizarTablaSolicitudes();
                                actualizarEstadisticas(PANEL_SOLICITUDES);
                                dialog.dispose();
                            }
                        });
                    } else {
                        actionButton = new JButton("Cerrar");
                        actionButton.setBackground(new Color(108, 117, 125));
                        actionButton.setForeground(Color.WHITE);
                        actionButton.setFocusPainted(false);
                        actionButton.addActionListener(e -> dialog.dispose());
                    }
                } else {
                    actionButton = new JButton("Cerrar");
                    actionButton.setBackground(new Color(108, 117, 125));
                    actionButton.setForeground(Color.WHITE);
                    actionButton.setFocusPainted(false);
                    actionButton.addActionListener(e -> dialog.dispose());
                }

                buttonPanel.add(actionButton);
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
            Logger.getLogger(EmployeeMainFrame.class.getName()).log(Level.SEVERE, "Error occurred", e);
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

        List<Usuario> tecnicos = UsuarioDAO.getTecnicos();
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

        List<Usuario> tecnicos = UsuarioDAO.getTecnicos();
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

        List<String> clientesNombres = obtenerClientesUnicos();

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

    private List<String> obtenerClientesUnicos() {
        List<String> clientesUnicos = new ArrayList<>();
        List<Solicitud> solicitudes = SolicitudDAO.getSolicitudes();

        Set<String> nombresUnicos = new HashSet<>();

        for (Solicitud solicitud : solicitudes) {
            String nombreCliente = solicitud.getNombreSolicitante();
            if (nombreCliente != null && !nombreCliente.isEmpty()) {
                nombresUnicos.add(nombreCliente);
            }
        }

        clientesUnicos.addAll(nombresUnicos);
        Collections.sort(clientesUnicos);

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
        List<Solicitud> solicitudesCliente = obtenerSolicitudesPorCliente(nombreCliente);

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

    private List<Solicitud> obtenerSolicitudesPorCliente(String nombreCliente) {
        List<Solicitud> solicitudesCliente = new ArrayList<>();
        List<Solicitud> todasSolicitudes = SolicitudDAO.getSolicitudes();

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
        List<Solicitud> solicitudes = SolicitudDAO.getSolicitudes();

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
        JLabel lblUsuario = new JLabel(LoginController.getUsuarioLogueado().getUsuario());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(lblUsuario, infoGbc);

        infoGbc.gridx = 0;
        infoGbc.gridy = 1;
        userInfoPanel.add(new JLabel("Nombre:"), infoGbc);
        infoGbc.gridx = 1;
        JLabel lblNombre = new JLabel(LoginController.getUsuarioLogueado().getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(lblNombre, infoGbc);

        infoGbc.gridx = 0;
        infoGbc.gridy = 2;
        userInfoPanel.add(new JLabel("Rol:"), infoGbc);
        infoGbc.gridx = 1;
        JLabel lblRol = new JLabel(LoginController.getUsuarioLogueado().getRol().getNombre());
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
        final JLabel lblCorreoActualField = new JLabel();
        lblCorreoActualField.setFont(new Font("Arial", Font.ITALIC, 14));
        if (LoginController.getUsuarioLogueado().getCorreo() == null || LoginController.getUsuarioLogueado().getCorreo().isEmpty()) {
            lblCorreoActualField.setText("Sin asignar");
        } else {
            lblCorreoActualField.setText(LoginController.getUsuarioLogueado().getCorreo());
        }
        contentPanel.add(lblCorreoActualField, gbc);

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
        final JLabel lblTelefonoActualField = new JLabel();
        lblTelefonoActualField.setFont(new Font("Arial", Font.ITALIC, 14));
        if (LoginController.getUsuarioLogueado().getTelefono() == null || LoginController.getUsuarioLogueado().getTelefono().isEmpty()) {
            lblTelefonoActualField.setText("Sin asignar");
        } else {
            lblTelefonoActualField.setText(LoginController.getUsuarioLogueado().getTelefono());
        }
        contentPanel.add(lblTelefonoActualField, gbc);

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
        contentPanel.add(new JLabel("Confirmar Nueva Contraseña (*):"), gbc);
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
        btnActualizar.setPreferredSize(new Dimension(220, 40));

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
        JLabel lblInfo = new JLabel("(*) Contraseña actual es obligatoria para cambiar contraseña o para confirmar otros cambios.");
        lblInfo.setForeground(new Color(108, 117, 125));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(lblInfo, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(contentPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        btnActualizar.addActionListener(e -> {
            String nuevoCorreoInput = txtNuevoCorreo.getText().trim();
            String nuevoTelefonoInput = txtNuevoTelefono.getText().trim();
            String passActualInput = new String(pwdActual.getPassword());
            String passNuevaInput = new String(pwdNueva.getPassword());
            String passConfirmarInput = new String(pwdConfirmar.getPassword());

            Usuario usuarioLogueado = LoginController.getUsuarioLogueado();

            String correoParaActualizar = usuarioLogueado.getCorreo() != null ? usuarioLogueado.getCorreo() : "";
            String telefonoParaActualizar = usuarioLogueado.getTelefono() != null ? usuarioLogueado.getTelefono() : "";
            String nuevaContrasenaParaActualizar = null;

            boolean cambiosSolicitados = false;
            boolean intencionCambioCorreo = !nuevoCorreoInput.isEmpty();
            boolean intencionCambioTelefono = !nuevoTelefonoInput.isEmpty();
            boolean intencionCambioPassword = !passNuevaInput.isEmpty();

            if (intencionCambioCorreo) {
                if (!nuevoCorreoInput.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                    JOptionPane.showMessageDialog(panel, "El formato del nuevo correo electrónico no es válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!nuevoCorreoInput.equals(usuarioLogueado.getCorreo())) {
                    correoParaActualizar = nuevoCorreoInput;
                    cambiosSolicitados = true;
                }
            }

            if (intencionCambioTelefono) {
                String telefonoVerificado = verificarTelefono(nuevoTelefonoInput);
                if (telefonoVerificado.isEmpty() && !nuevoTelefonoInput.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "El formato del teléfono no es válido. Use solo números (7-15 dígitos) con o sin prefijo +", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!telefonoVerificado.equals(usuarioLogueado.getTelefono())) {
                    telefonoParaActualizar = telefonoVerificado;
                    cambiosSolicitados = true;
                }
            }

            if (intencionCambioPassword) {
                if (passActualInput.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Debe ingresar su contraseña actual para cambiarla.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!Objects.equals(passActualInput, usuarioLogueado.getContrasena())) {
                    JOptionPane.showMessageDialog(panel, "La contraseña actual ingresada es incorrecta.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!passNuevaInput.equals(passConfirmarInput)) {
                    JOptionPane.showMessageDialog(panel, "La nueva contraseña y su confirmación no coinciden.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (passNuevaInput.length() < 6) {
                    JOptionPane.showMessageDialog(panel, "La nueva contraseña debe tener al menos 6 caracteres.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                nuevaContrasenaParaActualizar = passNuevaInput;
                cambiosSolicitados = true;
            } else {
                if (!passActualInput.isEmpty() && !passConfirmarInput.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Si desea cambiar la contraseña, ingrese también la nueva contraseña.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (!cambiosSolicitados) {
                JOptionPane.showMessageDialog(panel, "No se ingresaron nuevos datos para actualizar o los datos son iguales a los actuales.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            boolean necesitaConfirmacionPassActual = (intencionCambioCorreo && !correoParaActualizar.equals(usuarioLogueado.getCorreo())) ||
                    (intencionCambioTelefono && !telefonoParaActualizar.equals(usuarioLogueado.getTelefono()));

            if (necesitaConfirmacionPassActual && !intencionCambioPassword) {
                if (passActualInput.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Debe ingresar su contraseña actual para confirmar los cambios de correo/teléfono.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!Objects.equals(passActualInput, usuarioLogueado.getContrasena())) {
                    JOptionPane.showMessageDialog(panel, "La contraseña actual es incorrecta para confirmar los cambios.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            boolean exitoActualizacionCredenciales = true;
            boolean exitoActualizacionPassword = true;

            boolean credencialesModificadas = intencionCambioCorreo && !correoParaActualizar.equals(usuarioLogueado.getCorreo() == null ? "" : usuarioLogueado.getCorreo()) ||
                    intencionCambioTelefono && !telefonoParaActualizar.equals(usuarioLogueado.getTelefono() == null ? "" : usuarioLogueado.getTelefono());

            if (credencialesModificadas) {
                Usuario usuarioConNuevasCredenciales = new Usuario();
                usuarioConNuevasCredenciales.setId(usuarioLogueado.getId());
                usuarioConNuevasCredenciales.setCorreo(correoParaActualizar);
                usuarioConNuevasCredenciales.setTelefono(telefonoParaActualizar);
                if (!UsuarioDAO.updateCredentials(usuarioConNuevasCredenciales)) {
                    exitoActualizacionCredenciales = false;
                }
            }

            if (intencionCambioPassword && nuevaContrasenaParaActualizar != null) {
                if (!new UsuarioDAO().updatePassword(usuarioLogueado.getId(), nuevaContrasenaParaActualizar)) {
                    exitoActualizacionPassword = false;
                }
            }

            if (exitoActualizacionCredenciales && exitoActualizacionPassword) {
                JOptionPane.showMessageDialog(panel, "Información actualizada correctamente.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);

                if (credencialesModificadas && exitoActualizacionCredenciales) {
                    if (intencionCambioCorreo && !correoParaActualizar.equals(usuarioLogueado.getCorreo() == null ? "" : usuarioLogueado.getCorreo())) {
                        if (UsuarioDAO.isEmailRegistered(correoParaActualizar)) {
                            JOptionPane.showMessageDialog(panel, "El correo electrónico ya está registrado para otro usuario.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                        } else {
                            LoginController.getUsuarioLogueado().setCorreo(correoParaActualizar);
                            lblCorreoActualField.setText(correoParaActualizar.isEmpty() ? "Sin asignar" : correoParaActualizar);
                        }
                    }
                    if (intencionCambioTelefono && !telefonoParaActualizar.equals(usuarioLogueado.getTelefono() == null ? "" : usuarioLogueado.getTelefono())) {
                        LoginController.getUsuarioLogueado().setTelefono(telefonoParaActualizar);
                        lblTelefonoActualField.setText(telefonoParaActualizar.isEmpty() ? "Sin asignar" : telefonoParaActualizar);
                    }
                }
                if (intencionCambioPassword && nuevaContrasenaParaActualizar != null && exitoActualizacionPassword) {
                    LoginController.getUsuarioLogueado().setContrasena(nuevaContrasenaParaActualizar);
                }

                txtNuevoCorreo.setText("");
                txtNuevoTelefono.setText("");
                pwdActual.setText("");
                pwdNueva.setText("");
                pwdConfirmar.setText("");
            } else {
                StringBuilder errorMsg = new StringBuilder("No se pudo actualizar la información completamente:\n");
                if (credencialesModificadas && !exitoActualizacionCredenciales) {
                    errorMsg.append("- Error al actualizar correo y/o teléfono.\n");
                }
                if (intencionCambioPassword && !exitoActualizacionPassword) {
                    errorMsg.append("- Error al actualizar la contraseña.\n");
                }
                JOptionPane.showMessageDialog(panel, errorMsg.toString(), "Error de Actualización", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> {
            txtNuevoCorreo.setText("");
            txtNuevoTelefono.setText("");
            pwdActual.setText("");
            pwdNueva.setText("");
            pwdConfirmar.setText("");
        });

        return panel;
    }

    private String verificarTelefono(String telefono) {
        if (telefono == null) {
            return "";
        }
        String telefonoLimpio = telefono.trim();

        if (telefonoLimpio.isEmpty()) {
            return "";
        }
        String regex = "^\\+?\\d{7,15}$";
        return telefonoLimpio.matches(regex) ? telefonoLimpio : "";
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

                        table.getColumnModel().getColumn(0).setPreferredWidth(50);
                        table.getColumnModel().getColumn(6).setPreferredWidth(250);

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
package com.ferremax.dao;

import com.ferremax.db.DatabaseConnection;
import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.Solicitud;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SolicitudDAO {
    public boolean crearSolicitud(Solicitud solicitud) {
        String sql = "INSERT INTO Solicitudes (nombre_solicitante, contacto, ubicacion, fecha, hora, estado) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, solicitud.getNombreSolicitante());
            pstmt.setString(2, solicitud.getContacto());
            pstmt.setString(3, solicitud.getUbicacion());
            pstmt.setDate(4, Date.valueOf(solicitud.getFecha())); // Convierte LocalDate a java.sql.Date
            pstmt.setTime(5, Time.valueOf(solicitud.getHora()));   // Convierte LocalTime a java.sql.Time
            pstmt.setString(6, solicitud.getEstado().name());    // Guarda el nombre del enum

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear solicitud: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }

    public Solicitud obtenerSolicitudPorId(int id) {
        // Asume tabla 'Solicitudes' y columnas 'id', 'nombre_solicitante', 'contacto', 'ubicacion', 'fecha', 'hora', 'estado'
        String sql = "SELECT id, nombre_solicitante, contacto, ubicacion, fecha, hora, estado FROM Solicitudes WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Solicitud solicitud = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                solicitud = mapResultSetToSolicitud(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener solicitud por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return solicitud;
    }

    public List<Solicitud> obtenerTodasSolicitudes() {
        String sql = "SELECT id, nombre_solicitante, contacto, ubicacion, fecha, hora, estado FROM Solicitudes ORDER BY fecha, hora";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Solicitud> solicitudes = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todas las solicitudes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        return solicitudes;
    }

    public List<Solicitud> obtenerSolicitudesPorFecha(LocalDate fecha) {
        // Asume tabla 'Solicitudes' y columnas 'id', 'nombre_solicitante', 'contacto', 'ubicacion', 'fecha', 'hora', 'estado'
        String sql = "SELECT id, nombre_solicitante, contacto, ubicacion, fecha, hora, estado FROM Solicitudes WHERE fecha = ? ORDER BY hora";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Solicitud> solicitudes = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(fecha)); // Usa la fecha como parámetro
            rs = pstmt.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes por fecha: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return solicitudes;
    }

    public List<Solicitud> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        // Asume tabla 'Solicitudes' y columnas 'id', 'nombre_solicitante', 'contacto', 'ubicacion', 'fecha', 'hora', 'estado'
        String sql = "SELECT id, nombre_solicitante, contacto, ubicacion, fecha, hora, estado FROM Solicitudes WHERE estado = ? ORDER BY fecha, hora";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Solicitud> solicitudes = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, estado.name()); // Usa el nombre del enum como parámetro
            rs = pstmt.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes por estado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return solicitudes;
    }

    public boolean actualizarSolicitud(Solicitud solicitud) {
        String sql = "UPDATE Solicitudes SET nombre_solicitante = ?, contacto = ?, ubicacion = ?, fecha = ?, hora = ?, estado = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, solicitud.getNombreSolicitante());
            pstmt.setString(2, solicitud.getContacto());
            pstmt.setString(3, solicitud.getUbicacion());
            pstmt.setDate(4, Date.valueOf(solicitud.getFecha()));
            pstmt.setTime(5, Time.valueOf(solicitud.getHora()));
            pstmt.setString(6, solicitud.getEstado().name());
            pstmt.setInt(7, solicitud.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar solicitud: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }

    public boolean eliminarSolicitud(int id) {
        // Asume tabla 'Solicitudes' y columna 'id'
        String sql = "DELETE FROM Solicitudes WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar solicitud: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }

    private Solicitud mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        // Asume columnas 'id', 'nombre_solicitante', 'contacto', 'ubicacion', 'fecha', 'hora', 'estado'
        return new Solicitud(
                rs.getInt("id"),
                rs.getString("nombre_solicitante"),
                rs.getString("contacto"),
                rs.getString("ubicacion"),
                rs.getDate("fecha").toLocalDate(), // Convierte java.sql.Date a LocalDate
                rs.getTime("hora").toLocalTime(),   // Convierte java.sql.Time a LocalTime
                EstadoSolicitud.valueOf(rs.getString("estado")) // Convierte String a Enum
        );
    }
}

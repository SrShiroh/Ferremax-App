package com.ferremax.dao;

import com.ferremax.db.DatabaseConnection;
import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.Solicitud;
import com.ferremax.util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolicitudDAO {
    public static int getEmpleadosA() {
        int empleadosA = 0;
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE activo = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                empleadosA = rs.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al contar empleados activos");
        }
        return empleadosA;
    }

    public static int getReparacionesReg() {
        int reparacionesReg = 0;
        String sql = "SELECT COUNT(*) FROM Solicitudes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                reparacionesReg = rs.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al contar el total de solicitudes");
        }
        return reparacionesReg;
    }

    public static int getReparacionesR() {
        int reparacionesR = 0;
        String sql = "SELECT COUNT(*) FROM Solicitudes WHERE id_estado = 4";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                reparacionesR = rs.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al contar reparaciones realizadas");
        }
        return reparacionesR;
    }

    public static int getSolicitudesP() {
        int solicitudesP = 0;
        String sql = "SELECT COUNT(*) FROM Solicitudes WHERE id_estado = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                solicitudesP = rs.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al contar solicitudes pendientes");
        }
        return solicitudesP;
    }

    public static Solicitud getById(int solicitudId) {
        Solicitud solicitud = null;
        String sql = "SELECT s.*, " +
                "reg.nombre as nombre_registrador, " +
                "tec.nombre as nombre_tecnico " +
                "FROM Solicitudes s " +
                "LEFT JOIN Usuarios reg ON s.id_usuario_registro = reg.id " +
                "LEFT JOIN Usuarios tec ON s.id_tecnico = tec.id " +
                "WHERE s.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, solicitudId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    solicitud = mapResultSetToSolicitud(rs);
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al obtener solicitud por ID: " + solicitudId);
        }
        return solicitud;
    }

    public static int create(Solicitud solicitud) {
        String sql = "INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, " +
                "fecha_solicitud, fecha_programada, hora_programada, id_estado, notas, " +
                "id_usuario_registro, id_tecnico) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, solicitud.getNombreSolicitante());
            stmt.setString(2, solicitud.getCorreo());
            stmt.setString(3, solicitud.getTelefono());
            stmt.setString(4, solicitud.getDireccion());
            stmt.setTimestamp(5, new Timestamp(solicitud.getFechaSolicitud().getTime()));

            if (solicitud.getFechaProgramada() != null) {
                stmt.setTimestamp(6, new Timestamp(solicitud.getFechaProgramada().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.TIMESTAMP);
            }

            stmt.setString(7, solicitud.getHoraProgramada());
            stmt.setInt(8, solicitud.getEstado().getId());
            stmt.setString(9, solicitud.getNotas());
            stmt.setInt(10, solicitud.getIdUsuarioRegistro());

            if (solicitud.getIdTecnico() > 0) {
                stmt.setInt(11, solicitud.getIdTecnico());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación de la solicitud falló, no se insertaron filas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("La creación de la solicitud falló, no se obtuvo el ID.");
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al crear solicitud");
            return -1;
        }
    }

    public static boolean update(Solicitud solicitud) {
        String sql = "UPDATE Solicitudes SET nombre_solicitante = ?, correo = ?, " +
                "telefono = ?, direccion = ?, fecha_programada = ?, " +
                "hora_programada = ?, id_estado = ?, notas = ?, id_tecnico = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solicitud.getNombreSolicitante());
            stmt.setString(2, solicitud.getCorreo());
            stmt.setString(3, solicitud.getTelefono());
            stmt.setString(4, solicitud.getDireccion());

            if (solicitud.getFechaProgramada() != null) {
                stmt.setTimestamp(5, new Timestamp(solicitud.getFechaProgramada().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.TIMESTAMP);
            }

            stmt.setString(6, solicitud.getHoraProgramada());
            stmt.setInt(7, solicitud.getEstado().getId());
            stmt.setString(8, solicitud.getNotas());

            if (solicitud.getIdTecnico() > 0) {
                stmt.setInt(9, solicitud.getIdTecnico());
            } else {
                stmt.setNull(9, java.sql.Types.INTEGER);
            }

            stmt.setInt(10, solicitud.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar solicitud: " + solicitud.getId());
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM Solicitudes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al eliminar solicitud: " + id);
            return false;
        }
    }

    public static List<Solicitud> getSolicitudes() {
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = "SELECT s.*, " +
                "reg.nombre as nombre_registrador, " +
                "tec.nombre as nombre_tecnico " +
                "FROM Solicitudes s " +
                "LEFT JOIN Usuarios reg ON s.id_usuario_registro = reg.id " +
                "LEFT JOIN Usuarios tec ON s.id_tecnico = tec.id " +
                "ORDER BY s.id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                solicitudes.add(mapResultSetToSolicitud(rs));
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al obtener todas las solicitudes");
        }
        return solicitudes;
    }

    private static Solicitud mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(rs.getInt("id"));
        solicitud.setNombreSolicitante(rs.getString("nombre_solicitante"));
        solicitud.setCorreo(rs.getString("correo"));
        solicitud.setTelefono(rs.getString("telefono"));
        solicitud.setDireccion(rs.getString("direccion"));

        Timestamp fechaSolicitud = rs.getTimestamp("fecha_solicitud");
        if (fechaSolicitud != null) {
            solicitud.setFechaSolicitud(new Date(fechaSolicitud.getTime()));
        }

        Timestamp fechaProgramada = rs.getTimestamp("fecha_programada");
        if (fechaProgramada != null) {
            solicitud.setFechaProgramada(new Date(fechaProgramada.getTime()));
        }

        solicitud.setHoraProgramada(rs.getString("hora_programada"));
        solicitud.setEstado(EstadoSolicitud.valueOf(rs.getInt("id_estado")));
        solicitud.setNotas(rs.getString("notas"));
        solicitud.setIdUsuarioRegistro(rs.getInt("id_usuario_registro"));

        int idTecnico = rs.getInt("id_tecnico");
        if (!rs.wasNull()) {
            solicitud.setIdTecnico(idTecnico);
        }

        solicitud.setNombreUsuarioRegistro(rs.getString("nombre_registrador"));
        solicitud.setNombreTecnico(rs.getString("nombre_tecnico"));

        return solicitud;
    }

    public static void asignarTecnico(int idSolicitud, int idTecnico) {
        String sql = "UPDATE Solicitudes SET id_tecnico = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTecnico);
            stmt.setInt(2, idSolicitud);
            stmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al asignar técnico a la solicitud: " + idSolicitud);
        }
    }
}

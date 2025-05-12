package com.ferremax.dao;

import com.ferremax.db.DatabaseConnection;
import com.ferremax.model.Horario;
import com.ferremax.model.Usuario;
import com.ferremax.util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HorarioDAO {

    public Horario findById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "SELECT * FROM Horarios WHERE id = ?"
            );
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToHorario(rs);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar horario por ID: " + id);
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public int create(Horario horario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "INSERT INTO Horarios (fecha, hora_inicio, hora_fin, disponible, id_solicitud) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            stmt.setTimestamp(1, new Timestamp(horario.getFecha().getTime()));
            stmt.setString(2, horario.getHoraInicio());
            stmt.setString(3, horario.getHoraFin());
            stmt.setBoolean(4, horario.isDisponible());

            if (horario.getIdSolicitud() != null) {
                stmt.setInt(5, horario.getIdSolicitud());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del horario falló, no se insertaron filas.");
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("La creación del horario falló, no se obtuvo el ID.");
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al crear horario");
            return -1;
        } finally {
            DatabaseConnection.closeResultSet(generatedKeys);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean update(Horario horario) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Horarios SET fecha = ?, hora_inicio = ?, hora_fin = ?, " +
                            "disponible = ?, id_solicitud = ? WHERE id = ?"
            );

            stmt.setTimestamp(1, new Timestamp(horario.getFecha().getTime()));
            stmt.setString(2, horario.getHoraInicio());
            stmt.setString(3, horario.getHoraFin());
            stmt.setBoolean(4, horario.isDisponible());

            if (horario.getIdSolicitud() != null) {
                stmt.setInt(5, horario.getIdSolicitud());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            stmt.setInt(6, horario.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar horario: " + horario.getId());
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Verificar primero si el horario está disponible
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT disponible FROM Horarios WHERE id = ?"
            );
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next() || !rs.getBoolean("disponible")) {
                // No existe o no está disponible
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(checkStmt);
                return false;
            }

            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(checkStmt);

            // Proceder con la eliminación
            stmt = conn.prepareStatement(
                    "DELETE FROM Horarios WHERE id = ?"
            );

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al eliminar horario: " + id);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public List<Horario> findAll() {
        return findByCriteria("", null);
    }

    public List<Horario> findByDate(Date fecha) {
        return findByCriteria("WHERE DATE(fecha) = DATE(?)",
                new Object[]{new Timestamp(fecha.getTime())});
    }

    public List<Horario> findAvailable() {
        return findByCriteria("WHERE disponible = true AND fecha >= CURDATE()", null);
    }

    public List<Horario> findBySolicitud(int idSolicitud) {
        return findByCriteria("WHERE id_solicitud = ?", new Object[]{idSolicitud});
    }

    public List<Horario> findByDateRange(Date fechaInicio, Date fechaFin) {
        return findByCriteria("WHERE fecha BETWEEN ? AND ?",
                new Object[]{
                        new Timestamp(fechaInicio.getTime()),
                        new Timestamp(fechaFin.getTime())
                });
    }

    public List<Horario> findAvailableByDate(Date fecha) {
        return findByCriteria(
                "WHERE disponible = true AND DATE(fecha) = DATE(?)",
                new Object[]{new Timestamp(fecha.getTime())}
        );
    }

    private List<Horario> findByCriteria(String whereClause, Object[] params) {
        List<Horario> horarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT h.*, s.nombre_solicitante " +
                    "FROM Horarios h " +
                    "LEFT JOIN Solicitudes s ON h.id_solicitud = s.id " +
                    whereClause +
                    " ORDER BY h.fecha, h.hora_inicio";

            stmt = conn.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                Horario horario = mapResultSetToHorario(rs);

                // Agregar información adicional si hay solicitud asociada
                if (horario.getIdSolicitud() != null) {
                    String nombreSolicitante = rs.getString("nombre_solicitante");
                    if (nombreSolicitante != null) {
                        // Se podría agregar esta información a un campo adicional en Horario
                    }
                }

                horarios.add(horario);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar horarios");
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return horarios;
    }

    public boolean asignarSolicitud(int horarioId, int solicitudId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Verificar primero si el horario está disponible
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT disponible FROM Horarios WHERE id = ?"
            );
            checkStmt.setInt(1, horarioId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next() || !rs.getBoolean("disponible")) {
                // No existe o no está disponible
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(checkStmt);
                return false;
            }

            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(checkStmt);

            // Proceder con la asignación
            stmt = conn.prepareStatement(
                    "UPDATE Horarios SET disponible = false, id_solicitud = ? WHERE id = ?"
            );

            stmt.setInt(1, solicitudId);
            stmt.setInt(2, horarioId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al asignar solicitud a horario: " + horarioId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean liberarHorario(int horarioId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Horarios SET disponible = true, id_solicitud = NULL WHERE id = ?"
            );

            stmt.setInt(1, horarioId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al liberar horario: " + horarioId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean liberarHorariosPorSolicitud(int solicitudId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Horarios SET disponible = true, id_solicitud = NULL WHERE id_solicitud = ?"
            );

            stmt.setInt(1, solicitudId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al liberar horarios por solicitud: " + solicitudId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean crearHorariosEnMasa(Date fecha, List<String[]> horarios) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = true;

        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction(conn);

            stmt = conn.prepareStatement(
                    "INSERT INTO Horarios (fecha, hora_inicio, hora_fin, disponible) " +
                            "VALUES (?, ?, ?, true)"
            );

            Timestamp fechaTimestamp = new Timestamp(fecha.getTime());

            for (String[] horario : horarios) {
                stmt.setTimestamp(1, fechaTimestamp);
                stmt.setString(2, horario[0]); // hora_inicio
                stmt.setString(3, horario[1]); // hora_fin

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    success = false;
                    break;
                }
            }

            if (success) {
                DatabaseConnection.commitTransaction(conn);
            } else {
                DatabaseConnection.rollbackTransaction(conn);
            }

            return success;
        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction(conn);
            ExceptionHandler.logException(e, "Error al crear horarios en masa");
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    private static Horario mapResultSetToHorario(ResultSet rs) throws SQLException {
        Horario horario = new Horario();
        horario.setId(rs.getInt("id"));

        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            horario.setFecha(new Date(fecha.getTime()));
        }

        horario.setHoraInicio(rs.getString("hora_inicio"));
        horario.setHoraFin(rs.getString("hora_fin"));
        horario.setDisponible(rs.getBoolean("disponible"));
        int idSolicitud = rs.getInt("id_solicitud");
        if (!rs.wasNull()) {
            horario.setIdSolicitud(idSolicitud);
        }

        return horario;
    }

    public int countAvailableByDate(Date fecha) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Horarios WHERE disponible = true AND DATE(fecha) = DATE(?)"
            );
            stmt.setTimestamp(1, new Timestamp(fecha.getTime()));
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al contar horarios disponibles por fecha");
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return 0;
    }

    public int countTotalByDate(Date fecha) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Horarios WHERE DATE(fecha) = DATE(?)"
            );
            stmt.setTimestamp(1, new Timestamp(fecha.getTime()));
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al contar total de horarios por fecha");
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return 0;
    }

    public boolean existeHorarioEnRango(Date fecha, String horaInicio, String horaFin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Horarios " +
                            "WHERE DATE(fecha) = DATE(?) " +
                            "AND ((hora_inicio <= ? AND hora_fin > ?) " +
                            "OR (hora_inicio < ? AND hora_fin >= ?) " +
                            "OR (hora_inicio >= ? AND hora_fin <= ?))"
            );

            stmt.setTimestamp(1, new Timestamp(fecha.getTime()));
            stmt.setString(2, horaInicio);
            stmt.setString(3, horaInicio);
            stmt.setString(4, horaFin);
            stmt.setString(5, horaFin);
            stmt.setString(6, horaInicio);
            stmt.setString(7, horaFin);

            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al verificar existencia de horario en rango");
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean deleteByDate(Date fecha) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Verificar primero si hay horarios ocupados en esa fecha
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Horarios WHERE DATE(fecha) = DATE(?) AND disponible = false"
            );
            checkStmt.setTimestamp(1, new Timestamp(fecha.getTime()));
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Hay horarios ocupados, no se puede eliminar
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(checkStmt);
                return false;
            }

            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(checkStmt);

            // Proceder con la eliminación de horarios disponibles
            stmt = conn.prepareStatement(
                    "DELETE FROM Horarios WHERE DATE(fecha) = DATE(?) AND disponible = true"
            );

            stmt.setTimestamp(1, new Timestamp(fecha.getTime()));

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al eliminar horarios por fecha");
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }
    //"ID", "Fecha", "Hora Inicio", "Hora Fin", "Disponible", "Solicitud", "Técnico Asignado", "Acciones"
    public static List<Horario> getHorarios() {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT h.*, s.id as solicitud_id, s.solicitante as nombre_solicitante, " +
                "u.nombre as nombre_tecnico " +
                "FROM horarios h " +
                "LEFT JOIN solicitudes s ON h.id_solicitud = s.id " +
                "LEFT JOIN usuarios u ON s.id_tecnico = u.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Horario horario = new Horario();
                horario.setId(rs.getInt("id"));
                horario.setFecha(rs.getDate("fecha"));
                horario.setHoraInicio(rs.getString("hora_inicio"));
                horario.setHoraFin(rs.getString("hora_fin"));
                horario.setDisponible(rs.getBoolean("disponible"));

                // Manejo de valores que pueden ser NULL
                int solicitudId = rs.getInt("solicitud_id");
                if (!rs.wasNull()) {
                    horario.setIdSolicitud(solicitudId);
                    horario.setTecnicoAsignado(rs.getString("nombre_tecnico"));
                }

                horarios.add(horario);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar horarios con detalles");
        }

        return horarios;
    }
}
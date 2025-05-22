package com.ferremax.dao;

import com.ferremax.db.DatabaseConnection;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Usuario;
import com.ferremax.util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioDAO {

    public static List<Usuario> getTecnicos() {
        List<Usuario> tecnicos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Usuarios WHERE id_rol = 3 ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {
                tecnicos.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al listar técnicos");
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return tecnicos;
    }

    public static Usuario findById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Usuarios WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar usuario por ID: " + id);
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public static boolean isEmailRegistered(String correoParaActualizar) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM Usuarios WHERE correo = ?");
            stmt.setString(1, correoParaActualizar);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al verificar correo registrado: " + correoParaActualizar);
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public Usuario findByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Usuarios WHERE usuario = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar usuario por nombre: " + username);
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    public static List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Usuarios ORDER BY nombre");

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al listar todos los usuarios");
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }

        return usuarios;
    }

    public boolean create(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "INSERT INTO Usuarios (usuario, nombre, correo, telefono, contrasena, " +
                            "id_rol, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getTelefono());

            stmt.setString(5, usuario.getContrasena());

            stmt.setInt(6, usuario.getRol().getId());
            stmt.setTimestamp(7, new Timestamp(usuario.getFechaRegistro().getTime()));
            stmt.setBoolean(8, usuario.isActivo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                usuario.setId(generatedKeys.getInt(1));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al crear usuario: " + usuario.getUsuario());
            return false;
        } finally {
            DatabaseConnection.closeResultSet(generatedKeys);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean update(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Usuarios SET usuario = ?, nombre = ?, correo = ?, " +
                            "telefono = ?, id_rol = ? WHERE id = ?"
            );

            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getTelefono());
            stmt.setInt(5, usuario.getRol().getId());
            stmt.setInt(6, usuario.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar usuario: " + usuario.getId());
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public static boolean updateCredentials(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Usuarios SET correo = ?, telefono = ? WHERE id = ?"
            );

            stmt.setString(1, usuario.getCorreo());
            stmt.setString(2, usuario.getTelefono());
            stmt.setInt(3, usuario.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar credenciales: " + usuario.getId());
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Usuarios SET contrasena = ? WHERE id = ?"
            );

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar contraseña: " + userId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean updateLastLogin(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(
                    "UPDATE Usuarios SET ultimo_acceso = ? WHERE id = ?"
            );

            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar último acceso: " + userId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean toggleActive(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            PreparedStatement getStmt = conn.prepareStatement(
                    "SELECT activo FROM Usuarios WHERE id = ?"
            );
            getStmt.setInt(1, userId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            boolean currentStatus = rs.getBoolean("activo");
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(getStmt);

            stmt = conn.prepareStatement(
                    "UPDATE Usuarios SET activo = ? WHERE id = ?"
            );

            stmt.setBoolean(1, !currentStatus);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al cambiar estado activo: " + userId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean delete(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Solicitudes WHERE id_tecnico = ?"
            );
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(checkStmt);
                return false;
            }

            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(checkStmt);

            checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM Solicitudes WHERE id_usuario_registro = ?"
            );
            checkStmt.setInt(1, userId);
            rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(checkStmt);
                return false;
            }

            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(checkStmt);

            stmt = conn.prepareStatement(
                    "DELETE FROM Usuarios WHERE id = ?"
            );

            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al eliminar usuario: " + userId);
            return false;
        } finally {
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    private static Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsuario(rs.getString("usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setRol(RolUsuario.valueOf(rs.getInt("id_rol")));
        usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));

        Timestamp ultimoAcceso = rs.getTimestamp("ultimo_acceso");
        if (ultimoAcceso != null) {
            usuario.setUltimoAcceso(new Date(ultimoAcceso.getTime()));
        }

        usuario.setActivo(rs.getBoolean("activo"));

        return usuario;
    }
}

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
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    public static List<Usuario> getTecnicos() {
        List<Usuario> tecnicos = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE id_rol = 3 ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tecnicos.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al listar técnicos");
        }
        return tecnicos;
    }

    public static Usuario findById(int id) {
        String sql = "SELECT * FROM Usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar usuario por ID: " + id);
        }
        return null;
    }

    public static boolean isEmailRegistered(String correoParaActualizar) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE correo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoParaActualizar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al verificar correo registrado: " + correoParaActualizar);
        }
        return false;
    }

    public static boolean verificarContrasena(int id, String passActualInput) {
        String sql = "SELECT contrasena FROM Usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("contrasena");
                    return BCrypt.checkpw(passActualInput, storedPassword);
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al verificar contraseña actual del usuario: " + id);
        }
        return false;
    }

    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM Usuarios WHERE usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al buscar usuario por nombre: " + username);
        }
        return null;
    }

    public static List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al listar todos los usuarios");
        }
        return usuarios;
    }

    public boolean create(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (usuario, nombre, correo, telefono, contrasena, " +
                "id_rol, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getTelefono());

            String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            stmt.setString(5, hashedPassword);

            stmt.setInt(6, usuario.getRol().getId());
            stmt.setTimestamp(7, new Timestamp(usuario.getFechaRegistro().getTime()));
            stmt.setBoolean(8, usuario.isActivo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al crear usuario: " + usuario.getUsuario());
            return false;
        }
    }

    public boolean update(Usuario usuario) {
        String sql = "UPDATE Usuarios SET usuario = ?, nombre = ?, correo = ?, " +
                "telefono = ?, id_rol = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        }
    }

    public static boolean updateCredentials(Usuario usuario) {
        String sql = "UPDATE Usuarios SET correo = ?, telefono = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getCorreo());
            stmt.setString(2, usuario.getTelefono());
            stmt.setInt(3, usuario.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar credenciales: " + usuario.getId());
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Usuarios SET contrasena = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar contraseña: " + userId);
            return false;
        }
    }

    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE Usuarios SET ultimo_acceso = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al actualizar último acceso: " + userId);
            return false;
        }
    }

    public boolean toggleActive(int userId) {
        String getStatusSql = "SELECT activo FROM Usuarios WHERE id = ?";
        String updateStatusSql = "UPDATE Usuarios SET activo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean currentStatus;
            try (PreparedStatement getStmt = conn.prepareStatement(getStatusSql)) {
                getStmt.setInt(1, userId);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                    currentStatus = rs.getBoolean("activo");
                }
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(updateStatusSql)) {
                updateStmt.setBoolean(1, !currentStatus);
                updateStmt.setInt(2, userId);
                int affectedRows = updateStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al cambiar estado activo: " + userId);
            return false;
        }
    }

    public boolean delete(int userId) {
        String checkTecnicoSql = "SELECT COUNT(*) FROM Solicitudes WHERE id_tecnico = ?";
        String checkUsuarioRegistroSql = "SELECT COUNT(*) FROM Solicitudes WHERE id_usuario_registro = ?";
        String deleteSql = "DELETE FROM Usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkTecnicoSql)) {
                checkStmt.setInt(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement checkStmt = conn.prepareStatement(checkUsuarioRegistroSql)) {
                checkStmt.setInt(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, userId);
                int affectedRows = deleteStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al eliminar usuario: " + userId);
            return false;
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

    public boolean registrarAdministrador(String nombre, String usuario, String correo, String contrasena) {
        String sql = "INSERT INTO Usuarios (usuario, nombre, correo, contrasena, id_rol, fecha_registro, activo) " +
                "VALUES (?, ?, ?, ?, 1, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, usuario);
                stmt.setString(2, nombre);
                stmt.setString(3, correo);
                String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());
                stmt.setString(4, hashedPassword);
                stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
                stmt.setBoolean(6, true);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                ExceptionHandler.logException(e, "Error al registrar administrador: " + usuario);
                return false;
            }
    }
    
    public static boolean checkAdminExists() {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE id_rol = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            ExceptionHandler.logException(e, "Error al verificar existencia de administrador");
        }
        return false;
    }
}

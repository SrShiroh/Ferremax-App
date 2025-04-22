package com.ferremax.dao;

import com.ferremax.db.DatabaseConnection;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public boolean crearUsuario(Usuario usuario) {

        String sql = "INSERT INTO Usuarios (nombre, correo, telefono, contrasena, rol) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getTelefono());
            pstmt.setString(4, usuario.getContrasena()); // Guarda contraseña en texto plano
            pstmt.setString(5, usuario.getRol().name()); // Guarda el nombre del enum

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }

    public Usuario obtenerUsuarioPorId(int id) {
        // Asume tabla 'Usuarios' y columnas 'id', 'nombre', 'correo', 'telefono', 'contrasena', 'rol'
        String sql = "SELECT id, nombre, correo, telefono, contrasena, id_rol FROM Usuarios WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return usuario;
    }

    public Usuario obtenerUsuarioPorNombre(String nombre) {
        String sql = "SELECT id, nombre, correo, telefono, contrasena, id_rol FROM Usuarios WHERE nombre = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return usuario;
    }
    public List<Usuario> obtenerTodosUsuarios() {
        String sql = "SELECT id, nombre, correo, telefono, contrasena, rol FROM Usuarios ORDER BY nombre";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        return usuarios;
    }
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET nombre = ?, correo = ?, telefono = ?, contrasena = ?, rol = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getTelefono());
            pstmt.setString(4, usuario.getContrasena()); // Actualiza contraseña en texto plano
            pstmt.setString(5, usuario.getRol().name());
            pstmt.setInt(6, usuario.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }

    public boolean eliminarUsuario(int id) {
        // Asume tabla 'Usuarios' y columna 'id'
        String sql = "DELETE FROM Usuarios WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }

    public Usuario validarCredenciales(String nombre, String contrasena) {
        String sql = "SELECT id, nombre, correo, telefono, contrasena, rol FROM Usuarios WHERE nombre = ? AND contrasena = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, contrasena);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return usuario;
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("contrasena"),
                RolUsuario.valueOf(rs.getString("rol"))
        );
    }
}

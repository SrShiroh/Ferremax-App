package com.ferremax.controller;

import com.ferremax.dao.UsuarioDAO;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Usuario;
import com.ferremax.security.SessionManager;
import com.ferremax.util.ExceptionHandler;
import com.ferremax.util.ValidationUtils;

import java.util.Date;
import java.util.List;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private Usuario currentUser;

    public UsuarioController() {
        usuarioDAO = new UsuarioDAO();
        currentUser = SessionManager.getLoggedInUser();
    }

    public List<Usuario> getAllUsuarios() {
        if (!currentUser.hasPermission("usuario.view")) {
            ExceptionHandler.showWarning("No tiene permisos para ver usuarios", "Error");
            return List.of();
        }
        return usuarioDAO.findAll();
    }

    public List<Usuario> getTecnicos() {
        if (!currentUser.hasPermission("usuario.view")) {
            ExceptionHandler.showWarning("No tiene permisos para ver técnicos", "Error");
            return List.of();
        }
        return usuarioDAO.findByRole(RolUsuario.TECNICO);
    }

    public boolean crearUsuario(Usuario usuario) {
        if (!currentUser.hasPermission("usuario.create")) {
            ExceptionHandler.showWarning("No tiene permisos para crear usuarios", "Error");
            return false;
        }

        // Validar campos requeridos
        if (ValidationUtils.isEmpty(usuario.getUsuario()) ||
                ValidationUtils.isEmpty(usuario.getNombre()) ||
                ValidationUtils.isEmpty(usuario.getContrasena()) ||
                usuario.getRol() == null) {
            ExceptionHandler.showWarning("Faltan campos obligatorios", "Validación");
            return false;
        }

        // Verificar si el nombre de usuario ya existe
        Usuario existente = usuarioDAO.findByUsername(usuario.getUsuario());
        if (existente != null) {
            ExceptionHandler.showWarning("El nombre de usuario ya existe", "Error");
            return false;
        }

        // Establecer fecha de registro
        usuario.setFechaRegistro(new Date());
        usuario.setActivo(true);

        // Guardar en texto plano
        return usuarioDAO.create(usuario);
    }
    public boolean cambiarContrasena(int usuarioId, String nuevaContrasena) {
        boolean puedoCambiar = currentUser.hasPermission("usuario.password.update") ||
                (currentUser.getId() == usuarioId &&
                        currentUser.hasPermission("usuario.update.self"));

        if (!puedoCambiar) {
            ExceptionHandler.showWarning("No tiene permisos para cambiar contraseñas", "Error");
            return false;
        }

        if (ValidationUtils.isEmpty(nuevaContrasena)) {
            ExceptionHandler.showWarning("La contraseña no puede estar vacía", "Error");
            return false;
        }

        // Actualizar con contraseña en texto plano
        return usuarioDAO.updatePassword(usuarioId, nuevaContrasena);
    }

    public boolean cambiarEstadoUsuario(int usuarioId) {
        if (!currentUser.hasPermission("usuario.toggle.active")) {
            ExceptionHandler.showWarning("No tiene permisos para activar/desactivar usuarios", "Error");
            return false;
        }

        if (usuarioId == currentUser.getId()) {
            ExceptionHandler.showWarning("No puede desactivar su propia cuenta", "Error");
            return false;
        }

        return usuarioDAO.toggleActive(usuarioId);
    }

    public boolean eliminarUsuario(int usuarioId) {
        if (!currentUser.hasPermission("usuario.delete")) {
            ExceptionHandler.showWarning("No tiene permisos para eliminar usuarios", "Error");
            return false;
        }

        if (usuarioId == currentUser.getId()) {
            ExceptionHandler.showWarning("No puede eliminar su propia cuenta", "Error");
            return false;
        }

        // Verificar que no tenga solicitudes asociadas
        // Aquí verificaríamos con SolicitudDAO si hay solicitudes asociadas
        // Por simplicidad, asumimos que podemos eliminar

        return usuarioDAO.delete(usuarioId);
    }

    public Usuario getUsuarioById(int id) {
        if (!currentUser.hasPermission("usuario.view") && currentUser.getId() != id) {
            ExceptionHandler.showWarning("No tiene permisos para ver este usuario", "Error");
            return null;
        }

        return usuarioDAO.findById(id);
    }

    public Usuario getCurrentUser() {
        return usuarioDAO.findById(currentUser.getId());
    }

    public boolean verificarContrasenaActual(int usuarioId, String contrasenaActual) {
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            return false;
        }

        // Comparación directa (texto plano)
        return usuario.getContrasena().equals(contrasenaActual);
    }
}

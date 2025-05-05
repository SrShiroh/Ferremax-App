package com.ferremax.controller;

import com.ferremax.dao.HorarioDAO;
import com.ferremax.dao.SolicitudDAO;
import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Solicitud;
import com.ferremax.model.Usuario;
import com.ferremax.security.SessionManager;
import com.ferremax.util.ExceptionHandler;
import com.ferremax.util.ValidationUtils;

import java.util.Date;
import java.util.List;

public class SolicitudController {
    private SolicitudDAO solicitudDAO;
    private HorarioDAO horarioDAO;
    private Usuario currentUser;

    public SolicitudController() {
        solicitudDAO = new SolicitudDAO();
        horarioDAO = new HorarioDAO();
        currentUser = SessionManager.getLoggedInUser();
    }

    public List<Solicitud> getAllSolicitudes () {
        if (currentUser.getRol() == RolUsuario.TECNICO) {
            // Los técnicos solo ven sus solicitudes asignadas
            return solicitudDAO.findByTechnician(currentUser.getId());
        }
        return solicitudDAO.findAll();
    }

    public List<Solicitud> getSolicitudesByEstado(EstadoSolicitud estado) {
        if (currentUser.getRol() == RolUsuario.TECNICO) {
            // Combinar criterios: por estado y por técnico
            List<Solicitud> solicitudes = solicitudDAO.findByTechnician(currentUser.getId());
            solicitudes.removeIf(s -> s.getEstado() != estado);
            return solicitudes;
        }
        return solicitudDAO.findByStatus(estado);
    }

    public List<Solicitud> searchSolicitudes(String term) {
        if (ValidationUtils.isEmpty(term)) {
            return getAllSolicitudes();
        }

        List<Solicitud> results = solicitudDAO.search(term);

        if (currentUser.getRol() == RolUsuario.TECNICO) {
            // Filtrar resultados solo para el técnico actual
            final int idTecnico = currentUser.getId();
            results.removeIf(s -> s.getIdTecnico() != idTecnico);
        }

        return results;
    }

    public boolean createSolicitud(Solicitud solicitud) {
        // Validar campos requeridos
        if (ValidationUtils.isEmpty(solicitud.getNombreSolicitante()) ||
                ValidationUtils.isEmpty(solicitud.getTelefono()) ||
                ValidationUtils.isEmpty(solicitud.getDireccion()) ||
                solicitud.getFechaProgramada() == null) {
            ExceptionHandler.showWarning("Faltan campos obligatorios", "Validación");
            return false;
        }

        // Asignar usuario que registra
        solicitud.setIdUsuarioRegistro(currentUser.getId());
        solicitud.setFechaSolicitud(new Date());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        // Crear la solicitud
        int id = solicitudDAO.create(solicitud);
        if (id > 0) {
            solicitud.setId(id);
            // Si tiene horario asignado, actualizarlo
            if (solicitud.getHoraProgramada() != null && !solicitud.getHoraProgramada().isEmpty()) {
                // Aquí se podría buscar y actualizar el horario correspondiente
            }
            return true;
        }
        return false;
    }

    public boolean updateSolicitud(Solicitud solicitud) {
        // Validar campos requeridos
        if (ValidationUtils.isEmpty(solicitud.getNombreSolicitante()) ||
                ValidationUtils.isEmpty(solicitud.getTelefono()) ||
                ValidationUtils.isEmpty(solicitud.getDireccion()) ||
                solicitud.getFechaProgramada() == null) {
            ExceptionHandler.showWarning("Faltan campos obligatorios", "Validación");
            return false;
        }

        Solicitud original = solicitudDAO.findById(solicitud.getId());
        if (original == null) {
            ExceptionHandler.showWarning("La solicitud no existe", "Error");
            return false;
        }

        // Validar permisos según el rol
        if (currentUser.getRol() == RolUsuario.TECNICO &&
                original.getIdTecnico() != currentUser.getId()) {
            ExceptionHandler.showWarning("No tiene permisos para modificar esta solicitud", "Error");
            return false;
        }

        // Si el estado ha cambiado, verificar transición válida
        if (original.getEstado() != solicitud.getEstado()) {
            if (!original.getEstado().canTransitionTo(solicitud.getEstado())) {
                ExceptionHandler.showWarning(
                        "No se puede cambiar de " + original.getEstado() +
                                " a " + solicitud.getEstado(),
                        "Error de Estado"
                );
                return false;
            }
        }

        return solicitudDAO.update(solicitud);
    }

    public boolean asignarTecnico(int solicitudId, int tecnicoId) {
        Solicitud solicitud = solicitudDAO.findById(solicitudId);
        if (solicitud == null) {
            ExceptionHandler.showWarning("La solicitud no existe", "Error");
            return false;
        }

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            ExceptionHandler.showWarning(
                    "Solo se pueden asignar técnicos a solicitudes pendientes",
                    "Error de Estado"
            );
            return false;
        }

        solicitud.setIdTecnico(tecnicoId);
        solicitud.setEstado(EstadoSolicitud.ASIGNADA);
        return solicitudDAO.update(solicitud);
    }

    public boolean cambiarEstado(int solicitudId, EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = solicitudDAO.findById(solicitudId);
        if (solicitud == null) {
            ExceptionHandler.showWarning("La solicitud no existe", "Error");
            return false;
        }

        // Validar transición de estado
        if (!solicitud.getEstado().canTransitionTo(nuevoEstado)) {
            ExceptionHandler.showWarning(
                    "No se puede cambiar de " + solicitud.getEstado() +
                            " a " + nuevoEstado,
                    "Error de Estado"
            );
            return false;
        }

        // Validar permisos según el rol
        if (currentUser.getRol() == RolUsuario.TECNICO &&
                solicitud.getIdTecnico() != currentUser.getId()) {
            ExceptionHandler.showWarning(
                    "No tiene permisos para modificar esta solicitud",
                    "Error"
            );
            return false;
        }

        return solicitudDAO.updateStatus(solicitudId, nuevoEstado);
    }

    public boolean deleteSolicitud(int solicitudId) {
        if (!currentUser.hasPermission("solicitud.delete")) {
            ExceptionHandler.showWarning("No tiene permisos para eliminar solicitudes", "Error");
            return false;
        }

        Solicitud solicitud = solicitudDAO.findById(solicitudId);
        if (solicitud == null) {
            ExceptionHandler.showWarning("La solicitud no existe", "Error");
            return false;
        }

        // Liberar el horario asociado si existe
        if (solicitud.getFechaProgramada() != null && solicitud.getHoraProgramada() != null) {
            // Aquí se buscaría el horario asociado y se liberaría
        }

        return solicitudDAO.delete(solicitudId);
    }

    public Solicitud getSolicitudById(int id) {
        return solicitudDAO.findById(id);
    }
}

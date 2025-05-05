package com.ferremax.util;

import com.ferremax.model.EstadoSolicitud;
import com.ferremax.model.RolUsuario;
import com.ferremax.model.Solicitud;
import com.ferremax.model.Usuario;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para validaciones comunes en la aplicación.
 * Proporciona métodos para validar formatos, datos requeridos y reglas de negocio.
 */
public class ValidationUtils {
    // Patrones de expresiones regulares
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{3}-?\\d{3}-?\\d{4}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{4,20}$");

    /**
     * Verifica si un correo electrónico tiene un formato válido
     * @param email El correo electrónico a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Verifica si un número de teléfono tiene un formato válido
     * @param phone El número de teléfono a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;

        // Eliminar todos los caracteres no numéricos para la validación
        String digits = phone.replaceAll("\\D", "");
        return digits.length() == 10 && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Verifica si un nombre de usuario tiene un formato válido
     * @param username El nombre de usuario a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Verifica si una cadena está vacía o es nula
     * @param text La cadena a verificar
     * @return true si la cadena es nula o vacía, false en caso contrario
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Formatea un número de teléfono al formato XXX-XXX-XXXX
     * @param phone El número de teléfono a formatear
     * @return El número formateado o el original si no se puede formatear
     */
    public static String formatPhone(String phone) {
        if (isEmpty(phone)) return "";

        // Eliminar todos los caracteres no numéricos
        String digits = phone.replaceAll("\\D", "");

        // Si tenemos 10 dígitos, formateamos como XXX-XXX-XXXX
        if (digits.length() == 10) {
            return digits.substring(0, 3) + "-" +
                    digits.substring(3, 6) + "-" +
                    digits.substring(6);
        }

        // De lo contrario, devolver lo que se ingresó
        return phone;
    }

    /**
     * Limpia una cadena de texto eliminando espacios al inicio y fin
     * @param text La cadena a limpiar
     * @return La cadena limpia o cadena vacía si es nula
     */
    public static String sanitizeText(String text) {
        if (text == null) return "";
        return text.trim();
    }

    /**
     * Valida los campos requeridos de un usuario
     * @param usuario El usuario a validar
     * @return true si todos los campos requeridos están presentes y válidos
     */
    public static boolean validateUsuario(Usuario usuario) {
        if (usuario == null) return false;

        // Validar campos requeridos
        if (isEmpty(usuario.getUsuario()) ||
                isEmpty(usuario.getNombre()) ||
                usuario.getRol() == null) {
            return false;
        }

        // Validar formato de nombre de usuario
        if (!isValidUsername(usuario.getUsuario())) {
            return false;
        }

        // Validar correo si está presente
        if (!isEmpty(usuario.getCorreo()) && !isValidEmail(usuario.getCorreo())) {
            return false;
        }

        // Validar teléfono si está presente
        if (!isEmpty(usuario.getTelefono()) && !isValidPhone(usuario.getTelefono())) {
            return false;
        }

        return true;
    }

    /**
     * Valida los campos requeridos de una solicitud
     * @param solicitud La solicitud a validar
     * @return true si todos los campos requeridos están presentes y válidos
     */
    public static boolean validateSolicitud(Solicitud solicitud) {
        if (solicitud == null) return false;

        // Validar campos requeridos
        if (isEmpty(solicitud.getNombreSolicitante()) ||
                isEmpty(solicitud.getTelefono()) ||
                isEmpty(solicitud.getDireccion())) {
            return false;
        }

        // Validar teléfono
        if (!isValidPhone(solicitud.getTelefono())) {
            return false;
        }

        // Validar correo si está presente
        if (!isEmpty(solicitud.getCorreo()) && !isValidEmail(solicitud.getCorreo())) {
            return false;
        }

        return true;
    }

    /**
     * Valida si una fecha es futura (posterior a la fecha actual)
     * @param date La fecha a validar
     * @return true si la fecha es futura, false en caso contrario
     */
    public static boolean isFutureDate(Date date) {
        if (date == null) return false;
        Date now = new Date();
        return date.after(now);
    }

    /**
     * Valida si una fecha es pasada (anterior a la fecha actual)
     * @param date La fecha a validar
     * @return true si la fecha es pasada, false en caso contrario
     */
    public static boolean isPastDate(Date date) {
        if (date == null) return false;
        Date now = new Date();
        return date.before(now);
    }

    /**
     * Valida si una contraseña cumple con los requisitos mínimos
     * @param password La contraseña a validar
     * @return true si la contraseña cumple los requisitos, false en caso contrario
     */
    public static boolean isValidPassword(String password) {
        // Mínimo 6 caracteres
        if (isEmpty(password) || password.length() < 6) {
            return false;
        }

        // Al menos un número
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Al menos una letra
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }

        return true;
    }

    /**
     * Valida si un tiempo en formato HH:MM es válido
     * @param time El tiempo a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidTime(String time) {
        if (isEmpty(time)) return false;

        // Verificar formato HH:MM
        if (!time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            return false;
        }

        return true;
    }

    /**
     * Valida si un rango de horas es válido (inicio antes que fin)
     * @param horaInicio Hora de inicio en formato HH:MM
     * @param horaFin Hora de fin en formato HH:MM
     * @return true si el rango es válido, false en caso contrario
     */
    public static boolean isValidTimeRange(String horaInicio, String horaFin) {
        if (!isValidTime(horaInicio) || !isValidTime(horaFin)) {
            return false;
        }

        // Convertir a minutos para comparar
        int minutosInicio = convertTimeToMinutes(horaInicio);
        int minutosFin = convertTimeToMinutes(horaFin);

        // La hora de fin debe ser posterior a la de inicio
        return minutosFin > minutosInicio;
    }

    /**
     * Convierte un tiempo en formato HH:MM a minutos totales
     * @param time El tiempo en formato HH:MM
     * @return El total de minutos
     */
    private static int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    /**
     * Valida si una transición de estado es válida según las reglas de negocio
     * @param estadoActual El estado actual de la solicitud
     * @param nuevoEstado El nuevo estado propuesto
     * @return true si la transición es válida, false en caso contrario
     */
    public static boolean isValidStatusTransition(EstadoSolicitud estadoActual, EstadoSolicitud nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) return false;

        // Si es el mismo estado, es válido
        if (estadoActual == nuevoEstado) return true;

        // Validar transiciones según reglas de negocio
        switch (estadoActual) {
            case PENDIENTE:
                // De PENDIENTE puede pasar a ASIGNADA o CANCELADA
                return nuevoEstado == EstadoSolicitud.ASIGNADA ||
                        nuevoEstado == EstadoSolicitud.CANCELADA;

            case ASIGNADA:
                // De ASIGNADA puede pasar a EN_PROCESO o CANCELADA
                return nuevoEstado == EstadoSolicitud.EN_PROCESO ||
                        nuevoEstado == EstadoSolicitud.CANCELADA;

            case EN_PROCESO:
                // De EN_PROCESO puede pasar a COMPLETADA o CANCELADA
                return nuevoEstado == EstadoSolicitud.COMPLETADA ||
                        nuevoEstado == EstadoSolicitud.CANCELADA;

            case COMPLETADA:
                // De COMPLETADA no puede cambiar a otro estado
                return false;

            case CANCELADA:
                // De CANCELADA no puede cambiar a otro estado
                return false;

            default:
                return false;
        }
    }

    /**
     * Valida si un usuario puede realizar una acción sobre una solicitud según su rol
     * @param rolUsuario El rol del usuario
     * @param accion La acción a realizar (crear, editar, asignar, etc.)
     * @return true si el usuario puede realizar la acción, false en caso contrario
     */
    public static boolean canPerformAction(RolUsuario rolUsuario, String accion) {
        if (rolUsuario == null || isEmpty(accion)) return false;

        switch (rolUsuario) {
            case ADMINISTRADOR:
                // El administrador puede hacer todo
                return true;

            case EMPLEADO:
                // El empleado puede crear, editar, ver, pero no asignar técnicos
                return !accion.equals("asignar_tecnico") &&
                        !accion.equals("eliminar");

            case TECNICO:
                // El técnico solo puede ver y actualizar sus propias solicitudes
                return accion.equals("ver") ||
                        accion.equals("actualizar_estado");

            default:
                return false;
        }
    }
}

package com.ferremax.model;

import java.util.HashMap;
import java.util.Map;

public enum RolUsuario {
    ADMINISTRADOR(1, "Administrador"),
    TECNICO(2, "Técnico"),
    EMPLEADO(3, "Empleado");

    private final int id;
    private final String nombre;

    private static final Map<Integer, RolUsuario> map = new HashMap<>();

    static {
        for (RolUsuario rol : RolUsuario.values()) {
            map.put(rol.id, rol);
        }
    }

    RolUsuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static RolUsuario valueOf(int id) {
        return map.get(id);
    }

    public boolean hasPermission(String permission) {
        switch (this) {
            case ADMINISTRADOR:
                return true; // Admin tiene todos los permisos
            case TECNICO:
                // Permisos del técnico
                return permission.startsWith("solicitud.view") ||
                        permission.equals("solicitud.update.estado") ||
                        permission.equals("horario.view");
            case EMPLEADO:
                // Permisos del empleado
                return permission.startsWith("solicitud") ||
                        permission.startsWith("horario") ||
                        permission.equals("usuario.update.self");
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}

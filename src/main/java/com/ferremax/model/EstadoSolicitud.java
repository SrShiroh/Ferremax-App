package com.ferremax.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum EstadoSolicitud {
    PENDIENTE(1, "Pendiente", new Color(255, 193, 7)),
    ASIGNADA(2, "Asignada", new Color(0, 123, 255)),
    EN_PROCESO(3, "En Proceso", new Color(23, 162, 184)),
    COMPLETADA(4, "Completada", new Color(40, 167, 69)),
    CANCELADA(5, "Cancelada", new Color(220, 53, 69));

    private final int id;
    private final String nombre;
    private final Color color;

    private static final Map<Integer, EstadoSolicitud> map = new HashMap<>();

    static {
        for (EstadoSolicitud estado : EstadoSolicitud.values()) {
            map.put(estado.id, estado);
        }
    }

    EstadoSolicitud(int id, String nombre, Color color) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public static EstadoSolicitud valueOf(int id) {
        return map.get(id);
    }

    public boolean canTransitionTo(EstadoSolicitud newStatus) {
        switch (this) {
            case PENDIENTE:
                return newStatus == ASIGNADA || newStatus == CANCELADA;
            case ASIGNADA:
                return newStatus == EN_PROCESO || newStatus == CANCELADA;
            case EN_PROCESO:
                return newStatus == COMPLETADA || newStatus == CANCELADA;
            case COMPLETADA:
            case CANCELADA:
                return false; // Estados finales
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}

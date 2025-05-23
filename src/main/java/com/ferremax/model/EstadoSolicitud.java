package com.ferremax.model;

import java.util.HashMap;
import java.util.Map;

public enum EstadoSolicitud {
    PENDIENTE(1, "Pendiente"),
    ASIGNADA(2, "Asignada"),
    EN_PROCESO(3, "En Proceso"),
    COMPLETADA(4, "Completada"),
    CANCELADA(5, "Cancelada");

    private final int id;
    private final String nombre;

    private static final Map<Integer, EstadoSolicitud> map = new HashMap<>();

    static {
        for (EstadoSolicitud estado : EstadoSolicitud.values()) {
            map.put(estado.id, estado);
        }
    }

    EstadoSolicitud(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
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
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}

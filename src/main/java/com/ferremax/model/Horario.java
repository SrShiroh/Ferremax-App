package com.ferremax.model;

import java.util.Date;

public class Horario {
    private int id;
    private Date fecha;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;
    private Integer idSolicitud; // Integer para permitir null

    // Constructor vacío
    public Horario() {
        this.disponible = true;
    }

    // Constructor completo
    public Horario(int id, Date fecha, String horaInicio, String horaFin,
                   boolean disponible, Integer idSolicitud) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = disponible;
        this.idSolicitud = idSolicitud;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
        this.disponible = (idSolicitud == null);
    }

    public void asignarSolicitud(int idSolicitud) {
        if (this.disponible) {
            this.idSolicitud = idSolicitud;
            this.disponible = false;
        }
    }

    public void liberarHorario() {
        this.idSolicitud = null;
        this.disponible = true;
    }

    @Override
    public String toString() {
        return fecha + " " + horaInicio + " - " + horaFin;
    }
}

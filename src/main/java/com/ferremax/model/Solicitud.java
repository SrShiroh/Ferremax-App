package com.ferremax.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Solicitud {

    private int id;
    private String nombreSolicitante;
    private String contacto;
    private String ubicacion;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoSolicitud estado;

    public Solicitud(String nombreSolicitante, String contacto, String ubicacion, LocalDate fecha, LocalTime hora, EstadoSolicitud estado) {
        this.nombreSolicitante = nombreSolicitante;
        this.contacto = contacto;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public Solicitud(int id, String nombreSolicitante, String contacto, String ubicacion, LocalDate fecha, LocalTime hora, EstadoSolicitud estado) {
        this.id = id;
        this.nombreSolicitante = nombreSolicitante;
        this.contacto = contacto;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", nombreSolicitante='" + nombreSolicitante + '\'' +
                ", contacto='" + contacto + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                '}';
    }
}

package com.ferremax.model;

import java.util.Date;

public class Solicitud {
    private int id;
    private String nombreSolicitante;
    private String correo;
    private String telefono;
    private String direccion;
    private Date fechaSolicitud;
    private Date fechaProgramada;
    private String horaProgramada;
    private EstadoSolicitud estado;
    private String notas;
    private int idUsuarioRegistro; // ID del empleado que registró la solicitud
    private int idTecnico; // ID del técnico asignado
    private String nombreTecnico; // Para mostrar en la UI
    private String nombreUsuarioRegistro; // Para mostrar en la UI

    // Constructor vacío
    public Solicitud() {
        this.fechaSolicitud = new Date();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    // Constructor completo
    public Solicitud(int id, String nombreSolicitante, String correo,
                     String telefono, String direccion, Date fechaSolicitud,
                     Date fechaProgramada, String horaProgramada,
                     EstadoSolicitud estado, String notas,
                     int idUsuarioRegistro, int idTecnico) {
        this.id = id;
        this.nombreSolicitante = nombreSolicitante;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaProgramada = fechaProgramada;
        this.horaProgramada = horaProgramada;
        this.estado = estado;
        this.notas = notas;
        this.idUsuarioRegistro = idUsuarioRegistro;
        this.idTecnico = idTecnico;
    }

    // Getters & Setters
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(Date fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getHoraProgramada() {
        return horaProgramada;
    }

    public void setHoraProgramada(String horaProgramada) {
        this.horaProgramada = horaProgramada;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getIdUsuarioRegistro() {
        return idUsuarioRegistro;
    }

    public void setIdUsuarioRegistro(int idUsuarioRegistro) {
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    public String getNombreUsuarioRegistro() {
        return nombreUsuarioRegistro;
    }

    public void setNombreUsuarioRegistro(String nombreUsuarioRegistro) {
        this.nombreUsuarioRegistro = nombreUsuarioRegistro;
    }

    public boolean cambiarEstado(EstadoSolicitud nuevoEstado) {
        if (estado.canTransitionTo(nuevoEstado)) {
            this.estado = nuevoEstado;
            return true;
        }
        return false;
    }

    public boolean asignarTecnico(int idTecnico) {
        if (this.estado == EstadoSolicitud.PENDIENTE) {
            this.idTecnico = idTecnico;
            this.estado = EstadoSolicitud.ASIGNADA;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Solicitud #" + id + " - " + nombreSolicitante;
    }
}

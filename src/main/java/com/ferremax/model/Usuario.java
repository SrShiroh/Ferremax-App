package com.ferremax.model;

import java.util.Date;

public class Usuario {
    private int id;
    private String usuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private RolUsuario rol;
    private Date fechaRegistro;
    private Date ultimoAcceso;
    private boolean activo;

    public Usuario() {
        this.fechaRegistro = new Date();
        this.activo = true;
    }

    public Usuario(int id, String usuario, String nombre, String correo,
                   String telefono, String contrasena, RolUsuario rol,
                   Date fechaRegistro, Date ultimoAcceso, boolean activo) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.ultimoAcceso = ultimoAcceso;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean hasPermission(String permission) {
        return rol != null && rol.hasPermission(permission);
    }

    @Override
    public String toString() {
        return nombre;
    }
}

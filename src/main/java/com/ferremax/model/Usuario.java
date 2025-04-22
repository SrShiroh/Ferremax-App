package com.ferremax.model;

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private RolUsuario rol;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String telefono, String contrasena, RolUsuario rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public Usuario(int id, String nombre, String correo, String telefono, String contrasena, RolUsuario rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getContrasena() { // Getter para la contraseña en texto plano
        return contrasena;
    }

    public void setContrasena(String contrasena) { // Setter para la contraseña en texto plano
        this.contrasena = contrasena;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", contrasena='********'" +
                ", rol=" + rol +
                '}';
    }
}

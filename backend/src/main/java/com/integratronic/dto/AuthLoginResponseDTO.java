package com.integratronic.dto;

public class AuthLoginResponseDTO {

    private Integer idUsuario;
    private String nombre;
    private String email;
    private String nombreRol;

    public AuthLoginResponseDTO() {
    }

    public AuthLoginResponseDTO(Integer idUsuario, String nombre, String email, String nombreRol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.nombreRol = nombreRol;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}

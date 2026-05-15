package com.integratronic.dto;

public class FamiliaProductoResponseDTO {

    private Integer idFamilia;
    private String nombre;
    private String descripcion;

    public FamiliaProductoResponseDTO() {
    }

    public FamiliaProductoResponseDTO(Integer idFamilia, String nombre, String descripcion) {
        this.idFamilia = idFamilia;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

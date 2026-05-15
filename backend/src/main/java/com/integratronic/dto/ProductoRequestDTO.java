package com.integratronic.dto;

import java.math.BigDecimal;

public class ProductoRequestDTO {

    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Boolean activo;
    private Integer idFamilia;

    public ProductoRequestDTO() {
    }

    public ProductoRequestDTO(String sku, String nombre, String descripcion, BigDecimal precio, Boolean activo,
            Integer idFamilia) {
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = activo;
        this.idFamilia = idFamilia;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }
}

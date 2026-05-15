package com.integratronic.dto;

public class RegistrarVentaLineaRequestDTO {

    private Integer idProducto;
    private Integer cantidad;

    public RegistrarVentaLineaRequestDTO() {
    }

    public RegistrarVentaLineaRequestDTO(Integer idProducto, Integer cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}

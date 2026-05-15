package com.integratronic.dto;

public class RegistrarPedidoOnlineLineaRequestDTO {

    private Integer idProducto;
    private Integer cantidad;

    public RegistrarPedidoOnlineLineaRequestDTO() {
    }

    public RegistrarPedidoOnlineLineaRequestDTO(Integer idProducto, Integer cantidad) {
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

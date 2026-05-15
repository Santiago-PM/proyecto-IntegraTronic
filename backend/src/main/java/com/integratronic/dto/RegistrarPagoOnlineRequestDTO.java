package com.integratronic.dto;

import java.math.BigDecimal;

public class RegistrarPagoOnlineRequestDTO {

    private Integer idPedido;
    private BigDecimal importe;
    private String estadoPago;
    private String metodoPago;

    public RegistrarPagoOnlineRequestDTO() {
    }

    public RegistrarPagoOnlineRequestDTO(Integer idPedido, BigDecimal importe, String estadoPago, String metodoPago) {
        this.idPedido = idPedido;
        this.importe = importe;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}

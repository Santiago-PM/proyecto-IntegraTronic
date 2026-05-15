package com.integratronic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoOnlineResponseDTO {

    private Integer idPago;
    private LocalDateTime fechaPago;
    private BigDecimal importe;
    private String estadoPago;
    private String metodoPago;
    private Integer idPedido;
    private String estadoPedido;

    public PagoOnlineResponseDTO() {
    }

    public PagoOnlineResponseDTO(Integer idPago, LocalDateTime fechaPago, BigDecimal importe, String estadoPago,
            String metodoPago, Integer idPedido, String estadoPedido) {
        this.idPago = idPago;
        this.fechaPago = fechaPago;
        this.importe = importe;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
        this.idPedido = idPedido;
        this.estadoPedido = estadoPedido;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
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

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
}

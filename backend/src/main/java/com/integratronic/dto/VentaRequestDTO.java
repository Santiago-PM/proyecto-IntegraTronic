package com.integratronic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaRequestDTO {

    private LocalDateTime fechaVenta;
    private BigDecimal descuento;
    private BigDecimal total;
    private Integer idUsuario;

    public VentaRequestDTO() {
    }

    public VentaRequestDTO(LocalDateTime fechaVenta, BigDecimal descuento, BigDecimal total, Integer idUsuario) {
        this.fechaVenta = fechaVenta;
        this.descuento = descuento;
        this.total = total;
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}

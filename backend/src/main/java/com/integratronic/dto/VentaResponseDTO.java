package com.integratronic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaResponseDTO {

    private Integer idVenta;
    private LocalDateTime fechaVenta;
    private BigDecimal descuento;
    private BigDecimal total;
    private Integer idUsuario;
    private String nombreUsuario;

    public VentaResponseDTO() {
    }

    public VentaResponseDTO(Integer idVenta, LocalDateTime fechaVenta, BigDecimal descuento, BigDecimal total,
            Integer idUsuario, String nombreUsuario) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.descuento = descuento;
        this.total = total;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}

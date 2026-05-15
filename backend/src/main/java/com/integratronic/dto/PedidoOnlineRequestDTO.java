package com.integratronic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoOnlineRequestDTO {

    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal total;
    private Integer idCliente;

    public PedidoOnlineRequestDTO() {
    }

    public PedidoOnlineRequestDTO(LocalDateTime fechaPedido, String estado, BigDecimal total, Integer idCliente) {
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.total = total;
        this.idCliente = idCliente;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
}

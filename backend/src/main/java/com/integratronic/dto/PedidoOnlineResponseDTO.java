package com.integratronic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoOnlineResponseDTO {

    private Integer idPedido;
    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal total;
    private Integer idCliente;
    private String nombreCliente;
    private String emailCliente;

    public PedidoOnlineResponseDTO() {
    }

    public PedidoOnlineResponseDTO(Integer idPedido, LocalDateTime fechaPedido, String estado, BigDecimal total,
            Integer idCliente, String nombreCliente, String emailCliente) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.total = total;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
}

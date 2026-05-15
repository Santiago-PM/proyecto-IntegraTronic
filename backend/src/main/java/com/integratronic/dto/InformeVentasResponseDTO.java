package com.integratronic.dto;

import java.math.BigDecimal;

public class InformeVentasResponseDTO {

    private Long totalVentasFisicas;
    private Long totalPedidosOnline;
    private Long totalPagosOnline;
    private BigDecimal importeTotalVentasFisicas;
    private BigDecimal importeTotalPedidosOnline;
    private BigDecimal importeTotalPagosOnline;

    public InformeVentasResponseDTO() {
    }

    public InformeVentasResponseDTO(Long totalVentasFisicas, Long totalPedidosOnline, Long totalPagosOnline,
            BigDecimal importeTotalVentasFisicas, BigDecimal importeTotalPedidosOnline,
            BigDecimal importeTotalPagosOnline) {
        this.totalVentasFisicas = totalVentasFisicas;
        this.totalPedidosOnline = totalPedidosOnline;
        this.totalPagosOnline = totalPagosOnline;
        this.importeTotalVentasFisicas = importeTotalVentasFisicas;
        this.importeTotalPedidosOnline = importeTotalPedidosOnline;
        this.importeTotalPagosOnline = importeTotalPagosOnline;
    }

    public Long getTotalVentasFisicas() {
        return totalVentasFisicas;
    }

    public void setTotalVentasFisicas(Long totalVentasFisicas) {
        this.totalVentasFisicas = totalVentasFisicas;
    }

    public Long getTotalPedidosOnline() {
        return totalPedidosOnline;
    }

    public void setTotalPedidosOnline(Long totalPedidosOnline) {
        this.totalPedidosOnline = totalPedidosOnline;
    }

    public Long getTotalPagosOnline() {
        return totalPagosOnline;
    }

    public void setTotalPagosOnline(Long totalPagosOnline) {
        this.totalPagosOnline = totalPagosOnline;
    }

    public BigDecimal getImporteTotalVentasFisicas() {
        return importeTotalVentasFisicas;
    }

    public void setImporteTotalVentasFisicas(BigDecimal importeTotalVentasFisicas) {
        this.importeTotalVentasFisicas = importeTotalVentasFisicas;
    }

    public BigDecimal getImporteTotalPedidosOnline() {
        return importeTotalPedidosOnline;
    }

    public void setImporteTotalPedidosOnline(BigDecimal importeTotalPedidosOnline) {
        this.importeTotalPedidosOnline = importeTotalPedidosOnline;
    }

    public BigDecimal getImporteTotalPagosOnline() {
        return importeTotalPagosOnline;
    }

    public void setImporteTotalPagosOnline(BigDecimal importeTotalPagosOnline) {
        this.importeTotalPagosOnline = importeTotalPagosOnline;
    }
}

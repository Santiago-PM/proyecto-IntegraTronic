package com.integratronic.dto;

import java.math.BigDecimal;

public class LineaVentaResponseDTO {

    private Integer idLineaVenta;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private Integer idVenta;
    private Integer idProducto;
    private String nombreProducto;
    private String skuProducto;

    public LineaVentaResponseDTO() {
    }

    public LineaVentaResponseDTO(Integer idLineaVenta, Integer cantidad, BigDecimal precioUnitario,
            BigDecimal subtotal, Integer idVenta, Integer idProducto, String nombreProducto, String skuProducto) {
        this.idLineaVenta = idLineaVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.skuProducto = skuProducto;
    }

    public Integer getIdLineaVenta() {
        return idLineaVenta;
    }

    public void setIdLineaVenta(Integer idLineaVenta) {
        this.idLineaVenta = idLineaVenta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getSkuProducto() {
        return skuProducto;
    }

    public void setSkuProducto(String skuProducto) {
        this.skuProducto = skuProducto;
    }
}

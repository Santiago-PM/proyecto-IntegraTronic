package com.integratronic.dto;

public class StockRequestDTO {

    private Integer cantidadDisponible;
    private Integer stockMinimo;
    private Integer idProducto;

    public StockRequestDTO() {
    }

    public StockRequestDTO(Integer cantidadDisponible, Integer stockMinimo, Integer idProducto) {
        this.cantidadDisponible = cantidadDisponible;
        this.stockMinimo = stockMinimo;
        this.idProducto = idProducto;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
}

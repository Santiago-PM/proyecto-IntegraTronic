package com.integratronic.dto;

public class StockResponseDTO {

    private Integer idStock;
    private Integer cantidadDisponible;
    private Integer stockMinimo;
    private Integer idProducto;
    private String nombreProducto;
    private String skuProducto;

    public StockResponseDTO() {
    }

    public StockResponseDTO(Integer idStock, Integer cantidadDisponible, Integer stockMinimo, Integer idProducto,
            String nombreProducto, String skuProducto) {
        this.idStock = idStock;
        this.cantidadDisponible = cantidadDisponible;
        this.stockMinimo = stockMinimo;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.skuProducto = skuProducto;
    }

    public Integer getIdStock() {
        return idStock;
    }

    public void setIdStock(Integer idStock) {
        this.idStock = idStock;
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

package com.integratronic.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "linea_pedido")
public class LineaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linea_pedido")
    private Integer idLineaPedido;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoOnline pedidoOnline;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    public LineaPedido() {
    }

    public LineaPedido(Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal, PedidoOnline pedidoOnline,
            Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.pedidoOnline = pedidoOnline;
        this.producto = producto;
    }

    public Integer getIdLineaPedido() {
        return idLineaPedido;
    }

    public void setIdLineaPedido(Integer idLineaPedido) {
        this.idLineaPedido = idLineaPedido;
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

    public PedidoOnline getPedidoOnline() {
        return pedidoOnline;
    }

    public void setPedidoOnline(PedidoOnline pedidoOnline) {
        this.pedidoOnline = pedidoOnline;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}

package com.integratronic.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pago_online")
public class PagoOnline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "importe", precision = 10, scale = 2, nullable = false)
    private BigDecimal importe;

    @Column(name = "estado_pago", length = 50, nullable = false)
    private String estadoPago;

    @Column(name = "metodo_pago", length = 50, nullable = false)
    private String metodoPago;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private PedidoOnline pedidoOnline;

    public PagoOnline() {
    }

    public PagoOnline(LocalDateTime fechaPago, BigDecimal importe, String estadoPago, String metodoPago,
            PedidoOnline pedidoOnline) {
        this.fechaPago = fechaPago;
        this.importe = importe;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
        this.pedidoOnline = pedidoOnline;
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

    public PedidoOnline getPedidoOnline() {
        return pedidoOnline;
    }

    public void setPedidoOnline(PedidoOnline pedidoOnline) {
        this.pedidoOnline = pedidoOnline;
    }
}

package com.integratronic.dto;

import java.math.BigDecimal;
import java.util.List;

public class RegistrarVentaRequestDTO {

    private Integer idUsuario;
    private BigDecimal descuento;
    private List<RegistrarVentaLineaRequestDTO> lineas;

    public RegistrarVentaRequestDTO() {
    }

    public RegistrarVentaRequestDTO(Integer idUsuario, BigDecimal descuento,
            List<RegistrarVentaLineaRequestDTO> lineas) {
        this.idUsuario = idUsuario;
        this.descuento = descuento;
        this.lineas = lineas;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public List<RegistrarVentaLineaRequestDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<RegistrarVentaLineaRequestDTO> lineas) {
        this.lineas = lineas;
    }
}

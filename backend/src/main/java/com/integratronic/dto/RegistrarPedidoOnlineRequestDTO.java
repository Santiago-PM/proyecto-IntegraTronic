package com.integratronic.dto;

import java.util.List;

public class RegistrarPedidoOnlineRequestDTO {

    private Integer idCliente;
    private List<RegistrarPedidoOnlineLineaRequestDTO> lineas;

    public RegistrarPedidoOnlineRequestDTO() {
    }

    public RegistrarPedidoOnlineRequestDTO(Integer idCliente,
            List<RegistrarPedidoOnlineLineaRequestDTO> lineas) {
        this.idCliente = idCliente;
        this.lineas = lineas;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public List<RegistrarPedidoOnlineLineaRequestDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<RegistrarPedidoOnlineLineaRequestDTO> lineas) {
        this.lineas = lineas;
    }
}

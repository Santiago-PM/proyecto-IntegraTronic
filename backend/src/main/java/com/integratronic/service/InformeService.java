package com.integratronic.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.integratronic.dto.InformeVentasResponseDTO;
import com.integratronic.model.PagoOnline;
import com.integratronic.model.PedidoOnline;
import com.integratronic.model.Venta;
import com.integratronic.repository.PagoOnlineRepository;
import com.integratronic.repository.PedidoOnlineRepository;
import com.integratronic.repository.VentaRepository;

@Service
public class InformeService {

    private final VentaRepository ventaRepository;
    private final PedidoOnlineRepository pedidoOnlineRepository;
    private final PagoOnlineRepository pagoOnlineRepository;

    public InformeService(VentaRepository ventaRepository, PedidoOnlineRepository pedidoOnlineRepository,
            PagoOnlineRepository pagoOnlineRepository) {
        this.ventaRepository = ventaRepository;
        this.pedidoOnlineRepository = pedidoOnlineRepository;
        this.pagoOnlineRepository = pagoOnlineRepository;
    }

    public InformeVentasResponseDTO obtenerInformeVentas() {
        Long totalVentasFisicas = ventaRepository.count();
        Long totalPedidosOnline = pedidoOnlineRepository.count();
        Long totalPagosOnline = pagoOnlineRepository.count();

        BigDecimal importeTotalVentasFisicas = ventaRepository.findAll().stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal importeTotalPedidosOnline = pedidoOnlineRepository.findAll().stream()
                .map(PedidoOnline::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal importeTotalPagosOnline = pagoOnlineRepository.findAll().stream()
                .map(PagoOnline::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InformeVentasResponseDTO(
                totalVentasFisicas,
                totalPedidosOnline,
                totalPagosOnline,
                importeTotalVentasFisicas,
                importeTotalPedidosOnline,
                importeTotalPagosOnline);
    }
}

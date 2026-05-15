package com.integratronic.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integratronic.dto.RegistrarPagoOnlineRequestDTO;
import com.integratronic.model.PagoOnline;
import com.integratronic.model.PedidoOnline;
import com.integratronic.repository.PagoOnlineRepository;
import com.integratronic.repository.PedidoOnlineRepository;

@Service
public class PagoOnlineService {

    private final PagoOnlineRepository pagoOnlineRepository;
    private final PedidoOnlineRepository pedidoOnlineRepository;

    public PagoOnlineService(PagoOnlineRepository pagoOnlineRepository, PedidoOnlineRepository pedidoOnlineRepository) {
        this.pagoOnlineRepository = pagoOnlineRepository;
        this.pedidoOnlineRepository = pedidoOnlineRepository;
    }

    public List<PagoOnline> listar() {
        return pagoOnlineRepository.findAll();
    }

    public Optional<PagoOnline> buscarPorId(Integer id) {
        return pagoOnlineRepository.findById(id);
    }

    public PagoOnline guardar(PagoOnline pagoOnline) {
        return pagoOnlineRepository.save(pagoOnline);
    }

    public void eliminar(Integer id) {
        pagoOnlineRepository.deleteById(id);
    }

    @Transactional
    public PagoOnline registrarPagoOnline(RegistrarPagoOnlineRequestDTO request) {
        validarRequestRegistrarPagoOnline(request);

        PedidoOnline pedidoOnline = pedidoOnlineRepository.findById(request.getIdPedido())
                .orElseThrow(() -> new IllegalArgumentException("Pedido online no encontrado"));

        if (pagoOnlineRepository.findByPedidoOnline_IdPedido(request.getIdPedido()).isPresent()) {
            throw new IllegalArgumentException("El pedido ya tiene un pago registrado");
        }

        if (request.getImporte().compareTo(pedidoOnline.getTotal()) != 0) {
            throw new IllegalArgumentException("El importe no coincide con el total del pedido");
        }

        PagoOnline pagoOnline = new PagoOnline(
                LocalDateTime.now(),
                request.getImporte(),
                request.getEstadoPago(),
                request.getMetodoPago(),
                pedidoOnline);

        return pagoOnlineRepository.save(pagoOnline);
    }

    private void validarRequestRegistrarPagoOnline(RegistrarPagoOnlineRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo");
        }

        if (request.getIdPedido() == null) {
            throw new IllegalArgumentException("El pedido es obligatorio");
        }

        if (request.getImporte() == null) {
            throw new IllegalArgumentException("El importe es obligatorio");
        }

        if (request.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que 0");
        }

        if (request.getEstadoPago() == null || request.getEstadoPago().isBlank()) {
            throw new IllegalArgumentException("El estado del pago es obligatorio");
        }

        if (request.getMetodoPago() == null || request.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException("El metodo de pago es obligatorio");
        }
    }
}

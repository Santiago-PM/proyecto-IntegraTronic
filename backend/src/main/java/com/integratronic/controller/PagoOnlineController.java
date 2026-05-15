package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.PagoOnlineRequestDTO;
import com.integratronic.dto.PagoOnlineResponseDTO;
import com.integratronic.model.PagoOnline;
import com.integratronic.model.PedidoOnline;
import com.integratronic.service.PagoOnlineService;
import com.integratronic.service.PedidoOnlineService;

@RestController
@RequestMapping("/api/pagos-online")
public class PagoOnlineController {

    private final PagoOnlineService pagoOnlineService;
    private final PedidoOnlineService pedidoOnlineService;

    public PagoOnlineController(PagoOnlineService pagoOnlineService, PedidoOnlineService pedidoOnlineService) {
        this.pagoOnlineService = pagoOnlineService;
        this.pedidoOnlineService = pedidoOnlineService;
    }

    @GetMapping
    public List<PagoOnlineResponseDTO> listar() {
        return pagoOnlineService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoOnlineResponseDTO> buscarPorId(@PathVariable Integer id) {
        return pagoOnlineService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoOnlineResponseDTO> guardar(@RequestBody PagoOnlineRequestDTO pagoOnlineRequestDTO) {
        if (pagoOnlineRequestDTO.getIdPedido() == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoOnline pedidoOnline = pedidoOnlineService.buscarPorId(pagoOnlineRequestDTO.getIdPedido()).orElse(null);
        if (pedidoOnline == null) {
            return ResponseEntity.badRequest().build();
        }

        PagoOnline pagoOnline = convertirAEntidad(pagoOnlineRequestDTO, pedidoOnline);
        PagoOnline pagoGuardado = pagoOnlineService.guardar(pagoOnline);
        return ResponseEntity.ok(convertirAResponseDTO(pagoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoOnlineResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody PagoOnlineRequestDTO pagoOnlineRequestDTO) {
        if (pagoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (pagoOnlineRequestDTO.getIdPedido() == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoOnline pedidoOnline = pedidoOnlineService.buscarPorId(pagoOnlineRequestDTO.getIdPedido()).orElse(null);
        if (pedidoOnline == null) {
            return ResponseEntity.badRequest().build();
        }

        PagoOnline pagoOnline = convertirAEntidad(pagoOnlineRequestDTO, pedidoOnline);
        pagoOnline.setIdPago(id);
        PagoOnline pagoActualizado = pagoOnlineService.guardar(pagoOnline);
        return ResponseEntity.ok(convertirAResponseDTO(pagoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (pagoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pagoOnlineService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PagoOnlineResponseDTO convertirAResponseDTO(PagoOnline pagoOnline) {
        PedidoOnline pedidoOnline = pagoOnline.getPedidoOnline();
        Integer idPedido = pedidoOnline != null ? pedidoOnline.getIdPedido() : null;
        String estadoPedido = pedidoOnline != null ? pedidoOnline.getEstado() : null;

        return new PagoOnlineResponseDTO(
                pagoOnline.getIdPago(),
                pagoOnline.getFechaPago(),
                pagoOnline.getImporte(),
                pagoOnline.getEstadoPago(),
                pagoOnline.getMetodoPago(),
                idPedido,
                estadoPedido);
    }

    private PagoOnline convertirAEntidad(PagoOnlineRequestDTO pagoOnlineRequestDTO, PedidoOnline pedidoOnline) {
        return new PagoOnline(
                pagoOnlineRequestDTO.getFechaPago(),
                pagoOnlineRequestDTO.getImporte(),
                pagoOnlineRequestDTO.getEstadoPago(),
                pagoOnlineRequestDTO.getMetodoPago(),
                pedidoOnline);
    }
}

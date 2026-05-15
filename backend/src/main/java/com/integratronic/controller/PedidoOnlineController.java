package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.PedidoOnlineRequestDTO;
import com.integratronic.dto.PedidoOnlineResponseDTO;
import com.integratronic.dto.RegistrarPedidoOnlineRequestDTO;
import com.integratronic.model.Cliente;
import com.integratronic.model.PedidoOnline;
import com.integratronic.service.ClienteService;
import com.integratronic.service.PedidoOnlineService;

@RestController
@RequestMapping("/api/pedidos-online")
public class PedidoOnlineController {

    private final PedidoOnlineService pedidoOnlineService;
    private final ClienteService clienteService;

    public PedidoOnlineController(PedidoOnlineService pedidoOnlineService, ClienteService clienteService) {
        this.pedidoOnlineService = pedidoOnlineService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<PedidoOnlineResponseDTO> listar() {
        return pedidoOnlineService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoOnlineResponseDTO> buscarPorId(@PathVariable Integer id) {
        return pedidoOnlineService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public ResponseEntity<PedidoOnlineResponseDTO> registrarPedidoOnline(
            @RequestBody RegistrarPedidoOnlineRequestDTO request) {
        try {
            PedidoOnline pedidoGuardado = pedidoOnlineService.registrarPedidoOnline(request);
            return ResponseEntity.ok(convertirAResponseDTO(pedidoGuardado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<PedidoOnlineResponseDTO> guardar(
            @RequestBody PedidoOnlineRequestDTO pedidoOnlineRequestDTO) {
        if (pedidoOnlineRequestDTO.getIdCliente() == null) {
            return ResponseEntity.badRequest().build();
        }

        Cliente cliente = clienteService.buscarPorId(pedidoOnlineRequestDTO.getIdCliente()).orElse(null);
        if (cliente == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoOnline pedidoOnline = convertirAEntidad(pedidoOnlineRequestDTO, cliente);
        PedidoOnline pedidoGuardado = pedidoOnlineService.guardar(pedidoOnline);
        return ResponseEntity.ok(convertirAResponseDTO(pedidoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoOnlineResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody PedidoOnlineRequestDTO pedidoOnlineRequestDTO) {
        if (pedidoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (pedidoOnlineRequestDTO.getIdCliente() == null) {
            return ResponseEntity.badRequest().build();
        }

        Cliente cliente = clienteService.buscarPorId(pedidoOnlineRequestDTO.getIdCliente()).orElse(null);
        if (cliente == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoOnline pedidoOnline = convertirAEntidad(pedidoOnlineRequestDTO, cliente);
        pedidoOnline.setIdPedido(id);
        PedidoOnline pedidoActualizado = pedidoOnlineService.guardar(pedidoOnline);
        return ResponseEntity.ok(convertirAResponseDTO(pedidoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (pedidoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoOnlineService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoOnlineResponseDTO convertirAResponseDTO(PedidoOnline pedidoOnline) {
        Cliente cliente = pedidoOnline.getCliente();
        Integer idCliente = cliente != null ? cliente.getIdCliente() : null;
        String nombreCliente = cliente != null ? cliente.getNombre() : null;
        String emailCliente = cliente != null ? cliente.getEmail() : null;

        return new PedidoOnlineResponseDTO(
                pedidoOnline.getIdPedido(),
                pedidoOnline.getFechaPedido(),
                pedidoOnline.getEstado(),
                pedidoOnline.getTotal(),
                idCliente,
                nombreCliente,
                emailCliente);
    }

    private PedidoOnline convertirAEntidad(PedidoOnlineRequestDTO pedidoOnlineRequestDTO, Cliente cliente) {
        return new PedidoOnline(
                pedidoOnlineRequestDTO.getFechaPedido(),
                pedidoOnlineRequestDTO.getEstado(),
                pedidoOnlineRequestDTO.getTotal(),
                cliente);
    }
}

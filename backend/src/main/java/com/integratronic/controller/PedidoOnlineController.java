package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.PedidoOnline;
import com.integratronic.service.PedidoOnlineService;

@RestController
@RequestMapping("/api/pedidos-online")
public class PedidoOnlineController {

    private final PedidoOnlineService pedidoOnlineService;

    public PedidoOnlineController(PedidoOnlineService pedidoOnlineService) {
        this.pedidoOnlineService = pedidoOnlineService;
    }

    @GetMapping
    public List<PedidoOnline> listar() {
        return pedidoOnlineService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoOnline> buscarPorId(@PathVariable Integer id) {
        return pedidoOnlineService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PedidoOnline> guardar(@RequestBody PedidoOnline pedidoOnline) {
        PedidoOnline pedidoGuardado = pedidoOnlineService.guardar(pedidoOnline);
        return ResponseEntity.ok(pedidoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoOnline> actualizar(@PathVariable Integer id, @RequestBody PedidoOnline pedidoOnline) {
        if (pedidoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoOnline.setIdPedido(id);
        PedidoOnline pedidoActualizado = pedidoOnlineService.guardar(pedidoOnline);
        return ResponseEntity.ok(pedidoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (pedidoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoOnlineService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

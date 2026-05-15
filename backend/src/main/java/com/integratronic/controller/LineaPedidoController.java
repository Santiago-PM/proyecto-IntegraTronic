package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.LineaPedido;
import com.integratronic.service.LineaPedidoService;

@RestController
@RequestMapping("/api/lineas-pedido")
public class LineaPedidoController {

    private final LineaPedidoService lineaPedidoService;

    public LineaPedidoController(LineaPedidoService lineaPedidoService) {
        this.lineaPedidoService = lineaPedidoService;
    }

    @GetMapping
    public List<LineaPedido> listar() {
        return lineaPedidoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaPedido> buscarPorId(@PathVariable Integer id) {
        return lineaPedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LineaPedido> guardar(@RequestBody LineaPedido lineaPedido) {
        LineaPedido lineaGuardada = lineaPedidoService.guardar(lineaPedido);
        return ResponseEntity.ok(lineaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaPedido> actualizar(@PathVariable Integer id, @RequestBody LineaPedido lineaPedido) {
        if (lineaPedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lineaPedido.setIdLineaPedido(id);
        LineaPedido lineaActualizada = lineaPedidoService.guardar(lineaPedido);
        return ResponseEntity.ok(lineaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (lineaPedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lineaPedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

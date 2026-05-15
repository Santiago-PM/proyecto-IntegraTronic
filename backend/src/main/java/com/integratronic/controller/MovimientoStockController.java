package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.MovimientoStock;
import com.integratronic.service.MovimientoStockService;

@RestController
@RequestMapping("/api/movimientos-stock")
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;

    public MovimientoStockController(MovimientoStockService movimientoStockService) {
        this.movimientoStockService = movimientoStockService;
    }

    @GetMapping
    public List<MovimientoStock> listar() {
        return movimientoStockService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoStock> buscarPorId(@PathVariable Integer id) {
        return movimientoStockService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovimientoStock> guardar(@RequestBody MovimientoStock movimientoStock) {
        MovimientoStock movimientoGuardado = movimientoStockService.guardar(movimientoStock);
        return ResponseEntity.ok(movimientoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoStock> actualizar(@PathVariable Integer id,
            @RequestBody MovimientoStock movimientoStock) {
        if (movimientoStockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        movimientoStock.setIdMovimiento(id);
        MovimientoStock movimientoActualizado = movimientoStockService.guardar(movimientoStock);
        return ResponseEntity.ok(movimientoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (movimientoStockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        movimientoStockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.LineaVenta;
import com.integratronic.service.LineaVentaService;

@RestController
@RequestMapping("/api/lineas-venta")
public class LineaVentaController {

    private final LineaVentaService lineaVentaService;

    public LineaVentaController(LineaVentaService lineaVentaService) {
        this.lineaVentaService = lineaVentaService;
    }

    @GetMapping
    public List<LineaVenta> listar() {
        return lineaVentaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaVenta> buscarPorId(@PathVariable Integer id) {
        return lineaVentaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LineaVenta> guardar(@RequestBody LineaVenta lineaVenta) {
        LineaVenta lineaGuardada = lineaVentaService.guardar(lineaVenta);
        return ResponseEntity.ok(lineaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaVenta> actualizar(@PathVariable Integer id, @RequestBody LineaVenta lineaVenta) {
        if (lineaVentaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lineaVenta.setIdLineaVenta(id);
        LineaVenta lineaActualizada = lineaVentaService.guardar(lineaVenta);
        return ResponseEntity.ok(lineaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (lineaVentaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lineaVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

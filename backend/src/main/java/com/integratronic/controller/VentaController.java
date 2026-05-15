package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.Venta;
import com.integratronic.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Integer id) {
        return ventaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venta> guardar(@RequestBody Venta venta) {
        Venta ventaGuardada = ventaService.guardar(venta);
        return ResponseEntity.ok(ventaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Integer id, @RequestBody Venta venta) {
        if (ventaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        venta.setIdVenta(id);
        Venta ventaActualizada = ventaService.guardar(venta);
        return ResponseEntity.ok(ventaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (ventaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

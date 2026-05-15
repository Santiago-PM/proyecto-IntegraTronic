package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.FamiliaProducto;
import com.integratronic.service.FamiliaProductoService;

@RestController
@RequestMapping("/api/familias-producto")
public class FamiliaProductoController {

    private final FamiliaProductoService familiaProductoService;

    public FamiliaProductoController(FamiliaProductoService familiaProductoService) {
        this.familiaProductoService = familiaProductoService;
    }

    @GetMapping
    public List<FamiliaProducto> listar() {
        return familiaProductoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamiliaProducto> buscarPorId(@PathVariable Integer id) {
        return familiaProductoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FamiliaProducto> guardar(@RequestBody FamiliaProducto familiaProducto) {
        FamiliaProducto familiaGuardada = familiaProductoService.guardar(familiaProducto);
        return ResponseEntity.ok(familiaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamiliaProducto> actualizar(@PathVariable Integer id,
            @RequestBody FamiliaProducto familiaProducto) {
        if (familiaProductoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        familiaProducto.setIdFamilia(id);
        FamiliaProducto familiaActualizada = familiaProductoService.guardar(familiaProducto);
        return ResponseEntity.ok(familiaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (familiaProductoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        familiaProductoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.PagoOnline;
import com.integratronic.service.PagoOnlineService;

@RestController
@RequestMapping("/api/pagos-online")
public class PagoOnlineController {

    private final PagoOnlineService pagoOnlineService;

    public PagoOnlineController(PagoOnlineService pagoOnlineService) {
        this.pagoOnlineService = pagoOnlineService;
    }

    @GetMapping
    public List<PagoOnline> listar() {
        return pagoOnlineService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoOnline> buscarPorId(@PathVariable Integer id) {
        return pagoOnlineService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoOnline> guardar(@RequestBody PagoOnline pagoOnline) {
        PagoOnline pagoGuardado = pagoOnlineService.guardar(pagoOnline);
        return ResponseEntity.ok(pagoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoOnline> actualizar(@PathVariable Integer id, @RequestBody PagoOnline pagoOnline) {
        if (pagoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pagoOnline.setIdPago(id);
        PagoOnline pagoActualizado = pagoOnlineService.guardar(pagoOnline);
        return ResponseEntity.ok(pagoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (pagoOnlineService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pagoOnlineService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

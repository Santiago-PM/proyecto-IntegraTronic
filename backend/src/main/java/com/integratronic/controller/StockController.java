package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.model.Stock;
import com.integratronic.service.StockService;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> listar() {
        return stockService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> buscarPorId(@PathVariable Integer id) {
        return stockService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Stock> guardar(@RequestBody Stock stock) {
        Stock stockGuardado = stockService.guardar(stock);
        return ResponseEntity.ok(stockGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> actualizar(@PathVariable Integer id, @RequestBody Stock stock) {
        if (stockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        stock.setIdStock(id);
        Stock stockActualizado = stockService.guardar(stock);
        return ResponseEntity.ok(stockActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (stockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        stockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/incrementar")
    public ResponseEntity<Stock> incrementar(@PathVariable Integer id, @RequestParam Integer cantidad) {
        try {
            Stock stockActualizado = stockService.incrementarStock(id, cantidad);
            return ResponseEntity.ok(stockActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/reducir")
    public ResponseEntity<Stock> reducir(@PathVariable Integer id, @RequestParam Integer cantidad) {
        try {
            Stock stockActualizado = stockService.reducirStock(id, cantidad);
            return ResponseEntity.ok(stockActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

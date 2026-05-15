package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.StockRequestDTO;
import com.integratronic.dto.StockResponseDTO;
import com.integratronic.model.Producto;
import com.integratronic.model.Stock;
import com.integratronic.service.ProductoService;
import com.integratronic.service.StockService;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final ProductoService productoService;

    public StockController(StockService stockService, ProductoService productoService) {
        this.stockService = stockService;
        this.productoService = productoService;
    }

    @GetMapping
    public List<StockResponseDTO> listar() {
        return stockService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO> buscarPorId(@PathVariable Integer id) {
        return stockService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockResponseDTO> guardar(@RequestBody StockRequestDTO stockRequestDTO) {
        if (stockRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = productoService.buscarPorId(stockRequestDTO.getIdProducto()).orElse(null);
        if (producto == null) {
            return ResponseEntity.badRequest().build();
        }

        Stock stock = convertirAEntidad(stockRequestDTO, producto);
        Stock stockGuardado = stockService.guardar(stock);
        return ResponseEntity.ok(convertirAResponseDTO(stockGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody StockRequestDTO stockRequestDTO) {
        if (stockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (stockRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = productoService.buscarPorId(stockRequestDTO.getIdProducto()).orElse(null);
        if (producto == null) {
            return ResponseEntity.badRequest().build();
        }

        Stock stock = convertirAEntidad(stockRequestDTO, producto);
        stock.setIdStock(id);
        Stock stockActualizado = stockService.guardar(stock);
        return ResponseEntity.ok(convertirAResponseDTO(stockActualizado));
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
    public ResponseEntity<StockResponseDTO> incrementar(@PathVariable Integer id, @RequestParam Integer cantidad) {
        try {
            Stock stockActualizado = stockService.incrementarStock(id, cantidad);
            return ResponseEntity.ok(convertirAResponseDTO(stockActualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/reducir")
    public ResponseEntity<StockResponseDTO> reducir(@PathVariable Integer id, @RequestParam Integer cantidad) {
        try {
            Stock stockActualizado = stockService.reducirStock(id, cantidad);
            return ResponseEntity.ok(convertirAResponseDTO(stockActualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private StockResponseDTO convertirAResponseDTO(Stock stock) {
        Producto producto = stock.getProducto();
        Integer idProducto = producto != null ? producto.getIdProducto() : null;
        String nombreProducto = producto != null ? producto.getNombre() : null;
        String skuProducto = producto != null ? producto.getSku() : null;

        return new StockResponseDTO(
                stock.getIdStock(),
                stock.getCantidadDisponible(),
                stock.getStockMinimo(),
                idProducto,
                nombreProducto,
                skuProducto);
    }

    private Stock convertirAEntidad(StockRequestDTO stockRequestDTO, Producto producto) {
        return new Stock(
                stockRequestDTO.getCantidadDisponible(),
                stockRequestDTO.getStockMinimo(),
                producto);
    }
}

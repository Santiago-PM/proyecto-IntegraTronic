package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.MovimientoStockRequestDTO;
import com.integratronic.dto.MovimientoStockResponseDTO;
import com.integratronic.model.MovimientoStock;
import com.integratronic.model.Producto;
import com.integratronic.service.MovimientoStockService;
import com.integratronic.service.ProductoService;

@RestController
@RequestMapping("/api/movimientos-stock")
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;
    private final ProductoService productoService;

    public MovimientoStockController(MovimientoStockService movimientoStockService, ProductoService productoService) {
        this.movimientoStockService = movimientoStockService;
        this.productoService = productoService;
    }

    @GetMapping
    public List<MovimientoStockResponseDTO> listar() {
        return movimientoStockService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoStockResponseDTO> buscarPorId(@PathVariable Integer id) {
        return movimientoStockService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovimientoStockResponseDTO> guardar(
            @RequestBody MovimientoStockRequestDTO movimientoStockRequestDTO) {
        if (movimientoStockRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = productoService.buscarPorId(movimientoStockRequestDTO.getIdProducto()).orElse(null);
        if (producto == null) {
            return ResponseEntity.badRequest().build();
        }

        MovimientoStock movimientoStock = convertirAEntidad(movimientoStockRequestDTO, producto);
        MovimientoStock movimientoGuardado = movimientoStockService.guardar(movimientoStock);
        return ResponseEntity.ok(convertirAResponseDTO(movimientoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoStockResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody MovimientoStockRequestDTO movimientoStockRequestDTO) {
        if (movimientoStockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (movimientoStockRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = productoService.buscarPorId(movimientoStockRequestDTO.getIdProducto()).orElse(null);
        if (producto == null) {
            return ResponseEntity.badRequest().build();
        }

        MovimientoStock movimientoStock = convertirAEntidad(movimientoStockRequestDTO, producto);
        movimientoStock.setIdMovimiento(id);
        MovimientoStock movimientoActualizado = movimientoStockService.guardar(movimientoStock);
        return ResponseEntity.ok(convertirAResponseDTO(movimientoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (movimientoStockService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        movimientoStockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private MovimientoStockResponseDTO convertirAResponseDTO(MovimientoStock movimientoStock) {
        Producto producto = movimientoStock.getProducto();
        Integer idProducto = producto != null ? producto.getIdProducto() : null;
        String nombreProducto = producto != null ? producto.getNombre() : null;
        String skuProducto = producto != null ? producto.getSku() : null;

        return new MovimientoStockResponseDTO(
                movimientoStock.getIdMovimiento(),
                movimientoStock.getTipoMovimiento(),
                movimientoStock.getCantidad(),
                movimientoStock.getFechaMovimiento(),
                movimientoStock.getDescripcion(),
                idProducto,
                nombreProducto,
                skuProducto);
    }

    private MovimientoStock convertirAEntidad(MovimientoStockRequestDTO movimientoStockRequestDTO, Producto producto) {
        return new MovimientoStock(
                movimientoStockRequestDTO.getTipoMovimiento(),
                movimientoStockRequestDTO.getCantidad(),
                movimientoStockRequestDTO.getFechaMovimiento(),
                movimientoStockRequestDTO.getDescripcion(),
                producto);
    }
}

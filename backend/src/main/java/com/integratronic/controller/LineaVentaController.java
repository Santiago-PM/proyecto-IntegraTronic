package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.LineaVentaRequestDTO;
import com.integratronic.dto.LineaVentaResponseDTO;
import com.integratronic.model.LineaVenta;
import com.integratronic.model.Producto;
import com.integratronic.model.Venta;
import com.integratronic.service.LineaVentaService;
import com.integratronic.service.ProductoService;
import com.integratronic.service.VentaService;

@RestController
@RequestMapping("/api/lineas-venta")
public class LineaVentaController {

    private final LineaVentaService lineaVentaService;
    private final VentaService ventaService;
    private final ProductoService productoService;

    public LineaVentaController(LineaVentaService lineaVentaService, VentaService ventaService,
            ProductoService productoService) {
        this.lineaVentaService = lineaVentaService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @GetMapping
    public List<LineaVentaResponseDTO> listar() {
        return lineaVentaService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaVentaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return lineaVentaService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LineaVentaResponseDTO> guardar(@RequestBody LineaVentaRequestDTO lineaVentaRequestDTO) {
        if (lineaVentaRequestDTO.getIdVenta() == null || lineaVentaRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        Venta venta = ventaService.buscarPorId(lineaVentaRequestDTO.getIdVenta()).orElse(null);
        Producto producto = productoService.buscarPorId(lineaVentaRequestDTO.getIdProducto()).orElse(null);
        if (venta == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        LineaVenta lineaVenta = convertirAEntidad(lineaVentaRequestDTO, venta, producto);
        LineaVenta lineaGuardada = lineaVentaService.guardar(lineaVenta);
        return ResponseEntity.ok(convertirAResponseDTO(lineaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaVentaResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody LineaVentaRequestDTO lineaVentaRequestDTO) {
        if (lineaVentaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (lineaVentaRequestDTO.getIdVenta() == null || lineaVentaRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        Venta venta = ventaService.buscarPorId(lineaVentaRequestDTO.getIdVenta()).orElse(null);
        Producto producto = productoService.buscarPorId(lineaVentaRequestDTO.getIdProducto()).orElse(null);
        if (venta == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        LineaVenta lineaVenta = convertirAEntidad(lineaVentaRequestDTO, venta, producto);
        lineaVenta.setIdLineaVenta(id);
        LineaVenta lineaActualizada = lineaVentaService.guardar(lineaVenta);
        return ResponseEntity.ok(convertirAResponseDTO(lineaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (lineaVentaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lineaVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private LineaVentaResponseDTO convertirAResponseDTO(LineaVenta lineaVenta) {
        Venta venta = lineaVenta.getVenta();
        Producto producto = lineaVenta.getProducto();
        Integer idVenta = venta != null ? venta.getIdVenta() : null;
        Integer idProducto = producto != null ? producto.getIdProducto() : null;
        String nombreProducto = producto != null ? producto.getNombre() : null;
        String skuProducto = producto != null ? producto.getSku() : null;

        return new LineaVentaResponseDTO(
                lineaVenta.getIdLineaVenta(),
                lineaVenta.getCantidad(),
                lineaVenta.getPrecioUnitario(),
                lineaVenta.getSubtotal(),
                idVenta,
                idProducto,
                nombreProducto,
                skuProducto);
    }

    private LineaVenta convertirAEntidad(LineaVentaRequestDTO lineaVentaRequestDTO, Venta venta, Producto producto) {
        return new LineaVenta(
                lineaVentaRequestDTO.getCantidad(),
                lineaVentaRequestDTO.getPrecioUnitario(),
                lineaVentaRequestDTO.getSubtotal(),
                venta,
                producto);
    }
}

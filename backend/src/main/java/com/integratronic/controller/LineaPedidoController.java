package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.LineaPedidoRequestDTO;
import com.integratronic.dto.LineaPedidoResponseDTO;
import com.integratronic.model.LineaPedido;
import com.integratronic.model.PedidoOnline;
import com.integratronic.model.Producto;
import com.integratronic.service.LineaPedidoService;
import com.integratronic.service.PedidoOnlineService;
import com.integratronic.service.ProductoService;

@RestController
@RequestMapping("/api/lineas-pedido")
public class LineaPedidoController {

    private final LineaPedidoService lineaPedidoService;
    private final PedidoOnlineService pedidoOnlineService;
    private final ProductoService productoService;

    public LineaPedidoController(LineaPedidoService lineaPedidoService, PedidoOnlineService pedidoOnlineService,
            ProductoService productoService) {
        this.lineaPedidoService = lineaPedidoService;
        this.pedidoOnlineService = pedidoOnlineService;
        this.productoService = productoService;
    }

    @GetMapping
    public List<LineaPedidoResponseDTO> listar() {
        return lineaPedidoService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaPedidoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return lineaPedidoService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LineaPedidoResponseDTO> guardar(
            @RequestBody LineaPedidoRequestDTO lineaPedidoRequestDTO) {
        if (lineaPedidoRequestDTO.getIdPedido() == null || lineaPedidoRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoOnline pedidoOnline = pedidoOnlineService.buscarPorId(lineaPedidoRequestDTO.getIdPedido())
                .orElse(null);
        Producto producto = productoService.buscarPorId(lineaPedidoRequestDTO.getIdProducto()).orElse(null);
        if (pedidoOnline == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        LineaPedido lineaPedido = convertirAEntidad(lineaPedidoRequestDTO, pedidoOnline, producto);
        LineaPedido lineaGuardada = lineaPedidoService.guardar(lineaPedido);
        return ResponseEntity.ok(convertirAResponseDTO(lineaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaPedidoResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody LineaPedidoRequestDTO lineaPedidoRequestDTO) {
        if (lineaPedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (lineaPedidoRequestDTO.getIdPedido() == null || lineaPedidoRequestDTO.getIdProducto() == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoOnline pedidoOnline = pedidoOnlineService.buscarPorId(lineaPedidoRequestDTO.getIdPedido())
                .orElse(null);
        Producto producto = productoService.buscarPorId(lineaPedidoRequestDTO.getIdProducto()).orElse(null);
        if (pedidoOnline == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        LineaPedido lineaPedido = convertirAEntidad(lineaPedidoRequestDTO, pedidoOnline, producto);
        lineaPedido.setIdLineaPedido(id);
        LineaPedido lineaActualizada = lineaPedidoService.guardar(lineaPedido);
        return ResponseEntity.ok(convertirAResponseDTO(lineaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (lineaPedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lineaPedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private LineaPedidoResponseDTO convertirAResponseDTO(LineaPedido lineaPedido) {
        PedidoOnline pedidoOnline = lineaPedido.getPedidoOnline();
        Producto producto = lineaPedido.getProducto();
        Integer idPedido = pedidoOnline != null ? pedidoOnline.getIdPedido() : null;
        Integer idProducto = producto != null ? producto.getIdProducto() : null;
        String nombreProducto = producto != null ? producto.getNombre() : null;
        String skuProducto = producto != null ? producto.getSku() : null;

        return new LineaPedidoResponseDTO(
                lineaPedido.getIdLineaPedido(),
                lineaPedido.getCantidad(),
                lineaPedido.getPrecioUnitario(),
                lineaPedido.getSubtotal(),
                idPedido,
                idProducto,
                nombreProducto,
                skuProducto);
    }

    private LineaPedido convertirAEntidad(LineaPedidoRequestDTO lineaPedidoRequestDTO, PedidoOnline pedidoOnline,
            Producto producto) {
        return new LineaPedido(
                lineaPedidoRequestDTO.getCantidad(),
                lineaPedidoRequestDTO.getPrecioUnitario(),
                lineaPedidoRequestDTO.getSubtotal(),
                pedidoOnline,
                producto);
    }
}

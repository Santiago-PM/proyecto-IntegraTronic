package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.ProductoRequestDTO;
import com.integratronic.dto.ProductoResponseDTO;
import com.integratronic.model.FamiliaProducto;
import com.integratronic.model.Producto;
import com.integratronic.service.FamiliaProductoService;
import com.integratronic.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final FamiliaProductoService familiaProductoService;

    public ProductoController(ProductoService productoService, FamiliaProductoService familiaProductoService) {
        this.productoService = productoService;
        this.familiaProductoService = familiaProductoService;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return productoService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardar(@RequestBody ProductoRequestDTO productoRequestDTO) {
        if (productoRequestDTO.getIdFamilia() == null) {
            return ResponseEntity.badRequest().build();
        }

        FamiliaProducto familiaProducto = familiaProductoService.buscarPorId(productoRequestDTO.getIdFamilia())
                .orElse(null);
        if (familiaProducto == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = convertirAEntidad(productoRequestDTO, familiaProducto);
        Producto productoGuardado = productoService.guardar(producto);
        return ResponseEntity.ok(convertirAResponseDTO(productoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody ProductoRequestDTO productoRequestDTO) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (productoRequestDTO.getIdFamilia() == null) {
            return ResponseEntity.badRequest().build();
        }

        FamiliaProducto familiaProducto = familiaProductoService.buscarPorId(productoRequestDTO.getIdFamilia())
                .orElse(null);
        if (familiaProducto == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = convertirAEntidad(productoRequestDTO, familiaProducto);
        producto.setIdProducto(id);
        Producto productoActualizado = productoService.guardar(producto);
        return ResponseEntity.ok(convertirAResponseDTO(productoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        FamiliaProducto familiaProducto = producto.getFamiliaProducto();
        Integer idFamilia = familiaProducto != null ? familiaProducto.getIdFamilia() : null;
        String nombreFamilia = familiaProducto != null ? familiaProducto.getNombre() : null;

        return new ProductoResponseDTO(
                producto.getIdProducto(),
                producto.getSku(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getActivo(),
                idFamilia,
                nombreFamilia);
    }

    private Producto convertirAEntidad(ProductoRequestDTO productoRequestDTO, FamiliaProducto familiaProducto) {
        return new Producto(
                productoRequestDTO.getSku(),
                productoRequestDTO.getNombre(),
                productoRequestDTO.getDescripcion(),
                productoRequestDTO.getPrecio(),
                productoRequestDTO.getActivo(),
                familiaProducto);
    }
}

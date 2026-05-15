package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.VentaRequestDTO;
import com.integratronic.dto.VentaResponseDTO;
import com.integratronic.model.Usuario;
import com.integratronic.model.Venta;
import com.integratronic.service.UsuarioService;
import com.integratronic.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final UsuarioService usuarioService;

    public VentaController(VentaService ventaService, UsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<VentaResponseDTO> listar() {
        return ventaService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ventaService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> guardar(@RequestBody VentaRequestDTO ventaRequestDTO) {
        if (ventaRequestDTO.getIdUsuario() == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioService.buscarPorId(ventaRequestDTO.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Venta venta = convertirAEntidad(ventaRequestDTO, usuario);
        Venta ventaGuardada = ventaService.guardar(venta);
        return ResponseEntity.ok(convertirAResponseDTO(ventaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody VentaRequestDTO ventaRequestDTO) {
        if (ventaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (ventaRequestDTO.getIdUsuario() == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioService.buscarPorId(ventaRequestDTO.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Venta venta = convertirAEntidad(ventaRequestDTO, usuario);
        venta.setIdVenta(id);
        Venta ventaActualizada = ventaService.guardar(venta);
        return ResponseEntity.ok(convertirAResponseDTO(ventaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (ventaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private VentaResponseDTO convertirAResponseDTO(Venta venta) {
        Usuario usuario = venta.getUsuario();
        Integer idUsuario = usuario != null ? usuario.getIdUsuario() : null;
        String nombreUsuario = usuario != null ? usuario.getNombre() : null;

        return new VentaResponseDTO(
                venta.getIdVenta(),
                venta.getFechaVenta(),
                venta.getDescuento(),
                venta.getTotal(),
                idUsuario,
                nombreUsuario);
    }

    private Venta convertirAEntidad(VentaRequestDTO ventaRequestDTO, Usuario usuario) {
        return new Venta(
                ventaRequestDTO.getFechaVenta(),
                ventaRequestDTO.getDescuento(),
                ventaRequestDTO.getTotal(),
                usuario);
    }
}

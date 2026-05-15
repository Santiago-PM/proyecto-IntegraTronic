package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.FamiliaProductoRequestDTO;
import com.integratronic.dto.FamiliaProductoResponseDTO;
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
    public List<FamiliaProductoResponseDTO> listar() {
        return familiaProductoService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamiliaProductoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return familiaProductoService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FamiliaProductoResponseDTO> guardar(
            @RequestBody FamiliaProductoRequestDTO familiaProductoRequestDTO) {
        FamiliaProducto familiaProducto = convertirAEntidad(familiaProductoRequestDTO);
        FamiliaProducto familiaGuardada = familiaProductoService.guardar(familiaProducto);
        return ResponseEntity.ok(convertirAResponseDTO(familiaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamiliaProductoResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody FamiliaProductoRequestDTO familiaProductoRequestDTO) {
        if (familiaProductoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FamiliaProducto familiaProducto = convertirAEntidad(familiaProductoRequestDTO);
        familiaProducto.setIdFamilia(id);
        FamiliaProducto familiaActualizada = familiaProductoService.guardar(familiaProducto);
        return ResponseEntity.ok(convertirAResponseDTO(familiaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (familiaProductoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        familiaProductoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private FamiliaProductoResponseDTO convertirAResponseDTO(FamiliaProducto familiaProducto) {
        return new FamiliaProductoResponseDTO(
                familiaProducto.getIdFamilia(),
                familiaProducto.getNombre(),
                familiaProducto.getDescripcion());
    }

    private FamiliaProducto convertirAEntidad(FamiliaProductoRequestDTO familiaProductoRequestDTO) {
        return new FamiliaProducto(
                familiaProductoRequestDTO.getNombre(),
                familiaProductoRequestDTO.getDescripcion());
    }
}

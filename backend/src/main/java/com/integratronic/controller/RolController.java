package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.RolRequestDTO;
import com.integratronic.dto.RolResponseDTO;
import com.integratronic.model.Rol;
import com.integratronic.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolResponseDTO> listar() {
        return rolService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> buscarPorId(@PathVariable Integer id) {
        return rolService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RolResponseDTO> guardar(@RequestBody RolRequestDTO rolRequestDTO) {
        Rol rol = convertirAEntidad(rolRequestDTO);
        Rol rolGuardado = rolService.guardar(rol);
        return ResponseEntity.ok(convertirAResponseDTO(rolGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody RolRequestDTO rolRequestDTO) {
        if (rolService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Rol rol = convertirAEntidad(rolRequestDTO);
        rol.setIdRol(id);
        Rol rolActualizado = rolService.guardar(rol);
        return ResponseEntity.ok(convertirAResponseDTO(rolActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (rolService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RolResponseDTO convertirAResponseDTO(Rol rol) {
        return new RolResponseDTO(
                rol.getIdRol(),
                rol.getNombreRol(),
                rol.getDescripcion());
    }

    private Rol convertirAEntidad(RolRequestDTO rolRequestDTO) {
        return new Rol(
                rolRequestDTO.getNombreRol(),
                rolRequestDTO.getDescripcion());
    }
}

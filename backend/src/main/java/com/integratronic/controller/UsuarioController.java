package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.UsuarioRequestDTO;
import com.integratronic.dto.UsuarioResponseDTO;
import com.integratronic.model.Rol;
import com.integratronic.model.Usuario;
import com.integratronic.service.RolService;
import com.integratronic.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> guardar(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioRequestDTO.getIdRol() == null) {
            return ResponseEntity.badRequest().build();
        }

        Rol rol = rolService.buscarPorId(usuarioRequestDTO.getIdRol()).orElse(null);
        if (rol == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = convertirAEntidad(usuarioRequestDTO, rol);
        Usuario usuarioGuardado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(convertirAResponseDTO(usuarioGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (usuarioRequestDTO.getIdRol() == null) {
            return ResponseEntity.badRequest().build();
        }

        Rol rol = rolService.buscarPorId(usuarioRequestDTO.getIdRol()).orElse(null);
        if (rol == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = convertirAEntidad(usuarioRequestDTO, rol);
        usuario.setIdUsuario(id);
        Usuario usuarioActualizado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(convertirAResponseDTO(usuarioActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (usuarioService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        Rol rol = usuario.getRol();
        Integer idRol = rol != null ? rol.getIdRol() : null;
        String nombreRol = rol != null ? rol.getNombreRol() : null;

        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getActivo(),
                idRol,
                nombreRol);
    }

    private Usuario convertirAEntidad(UsuarioRequestDTO usuarioRequestDTO, Rol rol) {
        return new Usuario(
                usuarioRequestDTO.getNombre(),
                usuarioRequestDTO.getEmail(),
                usuarioRequestDTO.getPasswordHash(),
                usuarioRequestDTO.getActivo(),
                rol);
    }
}

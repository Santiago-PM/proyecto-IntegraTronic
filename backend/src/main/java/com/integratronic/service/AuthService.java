package com.integratronic.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.integratronic.dto.AuthLoginRequestDTO;
import com.integratronic.dto.AuthLoginResponseDTO;
import com.integratronic.model.Rol;
import com.integratronic.model.Usuario;
import com.integratronic.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AuthLoginResponseDTO login(AuthLoginRequestDTO request) {
        validarRequest(request);

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail().trim())
                .orElseThrow(this::credencialesInvalidas);

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw credencialesInvalidas();
        }

        String passwordHash = usuario.getPasswordHash();
        if (passwordHash == null || passwordHash.isBlank()) {
            throw credencialesInvalidas();
        }

        if (esPasswordCifrada(passwordHash)) {
            if (!passwordEncoder.matches(request.getPassword(), passwordHash)) {
                throw credencialesInvalidas();
            }
        } else if (request.getPassword().equals(passwordHash)) {
            // Compatibilidad temporal: migra passwords antiguos en texto plano a BCrypt.
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            usuarioRepository.save(usuario);
        } else {
            throw credencialesInvalidas();
        }

        Rol rol = usuario.getRol();
        String nombreRol = rol != null ? rol.getNombreRol() : null;

        return new AuthLoginResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                nombreRol);
    }

    private void validarRequest(AuthLoginRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La peticion de login es obligatoria");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }
    }

    private boolean esPasswordCifrada(String passwordHash) {
        return passwordHash.startsWith("$2a$")
                || passwordHash.startsWith("$2b$")
                || passwordHash.startsWith("$2y$");
    }

    private IllegalStateException credencialesInvalidas() {
        return new IllegalStateException("Credenciales invalidas");
    }
}

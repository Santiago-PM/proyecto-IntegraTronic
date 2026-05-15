package com.integratronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}

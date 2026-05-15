package com.integratronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}

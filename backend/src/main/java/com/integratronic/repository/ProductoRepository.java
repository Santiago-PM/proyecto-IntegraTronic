package com.integratronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}

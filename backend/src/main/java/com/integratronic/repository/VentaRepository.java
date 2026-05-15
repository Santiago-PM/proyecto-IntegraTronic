package com.integratronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
}

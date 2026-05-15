package com.integratronic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    Optional<Stock> findByProducto_IdProducto(Integer idProducto);
}

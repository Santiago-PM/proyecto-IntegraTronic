package com.integratronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}

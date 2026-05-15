package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.Stock;
import com.integratronic.repository.StockRepository;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> listar() {
        return stockRepository.findAll();
    }

    public Optional<Stock> buscarPorId(Integer id) {
        return stockRepository.findById(id);
    }

    public Stock guardar(Stock stock) {
        return stockRepository.save(stock);
    }

    public void eliminar(Integer id) {
        stockRepository.deleteById(id);
    }

    public Stock incrementarStock(Integer idStock, Integer cantidad) {
        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado"));

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }

        stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
        return stockRepository.save(stock);
    }

    public Stock reducirStock(Integer idStock, Integer cantidad) {
        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado"));

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }

        if (stock.getCantidadDisponible() < cantidad) {
            throw new IllegalArgumentException("No hay stock suficiente");
        }

        stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidad);
        return stockRepository.save(stock);
    }
}

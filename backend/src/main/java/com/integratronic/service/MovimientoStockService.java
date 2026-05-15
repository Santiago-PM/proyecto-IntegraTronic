package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.MovimientoStock;
import com.integratronic.repository.MovimientoStockRepository;

@Service
public class MovimientoStockService {

    private final MovimientoStockRepository movimientoStockRepository;

    public MovimientoStockService(MovimientoStockRepository movimientoStockRepository) {
        this.movimientoStockRepository = movimientoStockRepository;
    }

    public List<MovimientoStock> listar() {
        return movimientoStockRepository.findAll();
    }

    public Optional<MovimientoStock> buscarPorId(Integer id) {
        return movimientoStockRepository.findById(id);
    }

    public MovimientoStock guardar(MovimientoStock movimientoStock) {
        return movimientoStockRepository.save(movimientoStock);
    }

    public void eliminar(Integer id) {
        movimientoStockRepository.deleteById(id);
    }
}

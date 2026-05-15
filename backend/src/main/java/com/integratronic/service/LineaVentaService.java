package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.LineaVenta;
import com.integratronic.repository.LineaVentaRepository;

@Service
public class LineaVentaService {

    private final LineaVentaRepository lineaVentaRepository;

    public LineaVentaService(LineaVentaRepository lineaVentaRepository) {
        this.lineaVentaRepository = lineaVentaRepository;
    }

    public List<LineaVenta> listar() {
        return lineaVentaRepository.findAll();
    }

    public Optional<LineaVenta> buscarPorId(Integer id) {
        return lineaVentaRepository.findById(id);
    }

    public LineaVenta guardar(LineaVenta lineaVenta) {
        return lineaVentaRepository.save(lineaVenta);
    }

    public void eliminar(Integer id) {
        lineaVentaRepository.deleteById(id);
    }
}

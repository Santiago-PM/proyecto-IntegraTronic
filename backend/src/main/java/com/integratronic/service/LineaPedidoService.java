package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.LineaPedido;
import com.integratronic.repository.LineaPedidoRepository;

@Service
public class LineaPedidoService {

    private final LineaPedidoRepository lineaPedidoRepository;

    public LineaPedidoService(LineaPedidoRepository lineaPedidoRepository) {
        this.lineaPedidoRepository = lineaPedidoRepository;
    }

    public List<LineaPedido> listar() {
        return lineaPedidoRepository.findAll();
    }

    public Optional<LineaPedido> buscarPorId(Integer id) {
        return lineaPedidoRepository.findById(id);
    }

    public LineaPedido guardar(LineaPedido lineaPedido) {
        return lineaPedidoRepository.save(lineaPedido);
    }

    public void eliminar(Integer id) {
        lineaPedidoRepository.deleteById(id);
    }
}

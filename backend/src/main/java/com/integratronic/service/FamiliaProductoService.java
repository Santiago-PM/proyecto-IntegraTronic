package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.FamiliaProducto;
import com.integratronic.repository.FamiliaProductoRepository;

@Service
public class FamiliaProductoService {

    private final FamiliaProductoRepository familiaProductoRepository;

    public FamiliaProductoService(FamiliaProductoRepository familiaProductoRepository) {
        this.familiaProductoRepository = familiaProductoRepository;
    }

    public List<FamiliaProducto> listar() {
        return familiaProductoRepository.findAll();
    }

    public Optional<FamiliaProducto> buscarPorId(Integer id) {
        return familiaProductoRepository.findById(id);
    }

    public FamiliaProducto guardar(FamiliaProducto familiaProducto) {
        return familiaProductoRepository.save(familiaProducto);
    }

    public void eliminar(Integer id) {
        familiaProductoRepository.deleteById(id);
    }
}

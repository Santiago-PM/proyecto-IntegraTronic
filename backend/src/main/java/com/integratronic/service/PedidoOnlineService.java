package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.PedidoOnline;
import com.integratronic.repository.PedidoOnlineRepository;

@Service
public class PedidoOnlineService {

    private final PedidoOnlineRepository pedidoOnlineRepository;

    public PedidoOnlineService(PedidoOnlineRepository pedidoOnlineRepository) {
        this.pedidoOnlineRepository = pedidoOnlineRepository;
    }

    public List<PedidoOnline> listar() {
        return pedidoOnlineRepository.findAll();
    }

    public Optional<PedidoOnline> buscarPorId(Integer id) {
        return pedidoOnlineRepository.findById(id);
    }

    public PedidoOnline guardar(PedidoOnline pedidoOnline) {
        return pedidoOnlineRepository.save(pedidoOnline);
    }

    public void eliminar(Integer id) {
        pedidoOnlineRepository.deleteById(id);
    }
}

package com.integratronic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integratronic.model.PagoOnline;
import com.integratronic.repository.PagoOnlineRepository;

@Service
public class PagoOnlineService {

    private final PagoOnlineRepository pagoOnlineRepository;

    public PagoOnlineService(PagoOnlineRepository pagoOnlineRepository) {
        this.pagoOnlineRepository = pagoOnlineRepository;
    }

    public List<PagoOnline> listar() {
        return pagoOnlineRepository.findAll();
    }

    public Optional<PagoOnline> buscarPorId(Integer id) {
        return pagoOnlineRepository.findById(id);
    }

    public PagoOnline guardar(PagoOnline pagoOnline) {
        return pagoOnlineRepository.save(pagoOnline);
    }

    public void eliminar(Integer id) {
        pagoOnlineRepository.deleteById(id);
    }
}

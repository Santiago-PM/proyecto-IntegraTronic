package com.integratronic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integratronic.model.PagoOnline;

public interface PagoOnlineRepository extends JpaRepository<PagoOnline, Integer> {

    Optional<PagoOnline> findByPedidoOnline_IdPedido(Integer idPedido);
}

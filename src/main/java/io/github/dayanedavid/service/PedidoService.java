package io.github.dayanedavid.service;

import io.github.dayanedavid.domain.entity.Pedido;
import io.github.dayanedavid.domain.enums.StatusPedido;
import io.github.dayanedavid.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizarStatus(Integer id, StatusPedido statusPedido);

}

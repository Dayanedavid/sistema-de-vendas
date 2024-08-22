package io.github.dayanedavid.rest.controller;


import io.github.dayanedavid.domain.entity.ItemPedido;
import io.github.dayanedavid.domain.entity.Pedido;
import io.github.dayanedavid.domain.enums.StatusPedido;
import io.github.dayanedavid.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.dayanedavid.rest.dto.InformacaoItemPedidoDTO;
import io.github.dayanedavid.rest.dto.InformacoesPedidoDTO;
import io.github.dayanedavid.rest.dto.PedidoDTO;
import io.github.dayanedavid.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody @Validated PedidoDTO dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id){
        return service.obterPedidoCompleto(id)
                .map( p -> converter(p)).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    private InformacoesPedidoDTO converter(Pedido pedido){

        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens())).build();
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id
            , @RequestBody AtualizacaoStatusPedidoDTO dto){

        service.atualizarStatus(id, StatusPedido.valueOf(dto.getStatus()));
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens){

        if (itens.isEmpty()) {
            return Collections.emptyList();
        }

        return itens.stream()
            .map(x -> InformacaoItemPedidoDTO
                .builder()
                .quantidade(x.getQuantidade())
                .descricaoProduto(x.getProduto().getDescricao())
                .precoUnitario(x.getProduto().getPreco())
                .build()
            ).collect(Collectors.toList());
    }

}
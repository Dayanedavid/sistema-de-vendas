package io.github.dayanedavid.service.impl;

import io.github.dayanedavid.domain.entity.Cliente;
import io.github.dayanedavid.domain.entity.ItemPedido;
import io.github.dayanedavid.domain.entity.Pedido;
import io.github.dayanedavid.domain.entity.Produto;
import io.github.dayanedavid.domain.enums.StatusPedido;
import io.github.dayanedavid.domain.repository.Clientes;
import io.github.dayanedavid.domain.repository.ItemsPedido;
import io.github.dayanedavid.domain.repository.Pedidos;
import io.github.dayanedavid.domain.repository.Produtos;
import io.github.dayanedavid.exception.PedidoNaoEncontradoException;
import io.github.dayanedavid.exception.RegraNegocioException;
import io.github.dayanedavid.rest.dto.ItemPedidoDTO;
import io.github.dayanedavid.rest.dto.PedidoDTO;
import io.github.dayanedavid.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private Clientes clienteRepository;

    @Autowired
    private Produtos produtoRepository;

    @Autowired
    private Pedidos pedidoRepository;
    @Autowired
    private ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();

        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() ->
            new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedidos = converterItems(pedido, dto.getItems());

        pedidoRepository.save(pedido);
        itemsPedidoRepository.saveAll(itemPedidos);
        pedido.setItens(itemPedidos);

        return pedido;

    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id)
                .map(p -> {
                    p.setStatus(statusPedido);
                    return pedidoRepository.save(p);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items");
        }
        return  items
                .stream()
                .map(dto -> {

                    Integer idProduto = dto.getProduto();

                    Produto produto = produtoRepository.findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido" + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);

                    return itemPedido;
                }).collect(Collectors.toList());

    }

}

package io.github.dayanedavid.rest.dto;

import io.github.dayanedavid.validation.NotEmptyList;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

     @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
     private Integer cliente;

     @NotNull(message = "{campo.total-pedido.obrigatorio}")
     private BigDecimal total;

     @NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
     private List<ItemPedidoDTO> items;
}

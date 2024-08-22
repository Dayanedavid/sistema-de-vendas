package io.github.dayanedavid.domain.repository;

import io.github.dayanedavid.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Clientes extends JpaRepository<Cliente, Integer> {

    @Query(value = "SELECT * FROM Cliente c WHERE c.nome like '%:nome%' ", nativeQuery=true)
    List<Cliente> encontrarNome(@Param("nome") String nome);

    @Query(value = "delete FROM Cliente c WHERE c.nome = ':nome' ")
    @Modifying
    void deletePeloNome(@Param("nome") String nome);

    @Query(" select c from Cliente c left join fetch c.pedidos where c.id = :id ")
    Cliente findClienteFetchPedidos(@Param("id") Integer id);
}

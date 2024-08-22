package io.github.dayanedavid.domain.repository;

import io.github.dayanedavid.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {

}

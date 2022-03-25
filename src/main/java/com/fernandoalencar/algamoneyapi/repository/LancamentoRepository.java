package com.fernandoalencar.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.repository.lancamento.LancamentoRepositoryQuery;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

    List<Lancamento> findbyDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);

}

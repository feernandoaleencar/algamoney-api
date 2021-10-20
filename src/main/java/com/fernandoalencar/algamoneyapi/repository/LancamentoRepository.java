package com.fernandoalencar.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fernandoalencar.algamoneyapi.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}

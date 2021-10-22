package com.fernandoalencar.algamoneyapi.repository.lancamento;

import java.util.List;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}

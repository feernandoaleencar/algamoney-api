package com.fernandoalencar.algamoneyapi.repository.lancamento;

import com.fernandoalencar.algamoneyapi.dto.LancamentoEstatisticaCategoria;
import com.fernandoalencar.algamoneyapi.dto.LancamentoEstatisticaDia;
import com.fernandoalencar.algamoneyapi.dto.LancamentoEstatisticaPessoa;
import com.fernandoalencar.algamoneyapi.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.repository.filter.LancamentoFilter;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQuery {
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);

	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
}

package com.fernandoalencar.algamoneyapi.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fernandoalencar.algamoneyapi.event.RecursoCriadoEvent;
import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.repository.LancamentoRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	public ResponseEntity<Lancamento> salvar(Lancamento lancamento, HttpServletResponse response){
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
}

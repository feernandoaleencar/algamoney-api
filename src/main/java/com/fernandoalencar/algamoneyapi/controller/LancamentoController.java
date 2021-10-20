package com.fernandoalencar.algamoneyapi.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.repository.LancamentoRepository;
import com.fernandoalencar.algamoneyapi.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@PostMapping
	public ResponseEntity<Lancamento> cadastrar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
		return lancamentoService.salvar(lancamento, response);
	}
	
	@GetMapping
	public List<Lancamento> listar(){
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> buscar(@PathVariable Long id){
		
		Optional<Lancamento> lancamento = lancamentoRepository.findById(id);
		
		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}
	
}

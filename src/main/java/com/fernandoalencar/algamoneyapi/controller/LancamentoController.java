package com.fernandoalencar.algamoneyapi.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fernandoalencar.algamoneyapi.event.RecursoCriadoEvent;
import com.fernandoalencar.algamoneyapi.exceptionhandler.AlgaMoneyExceptionHandler.Erro;
import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.repository.LancamentoRepository;
import com.fernandoalencar.algamoneyapi.repository.filter.LancamentoFilter;
import com.fernandoalencar.algamoneyapi.service.LancamentoService;
import com.fernandoalencar.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;

	@PostMapping
	public ResponseEntity<Lancamento> cadastrar(@Valid @RequestBody Lancamento lancamento,
			HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter) {
		return lancamentoRepository.filtrar(lancamentoFilter);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> buscar(@PathVariable Long id) {

		Optional<Lancamento> lancamento = lancamentoRepository.findById(id);

		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlerPessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = this.messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return ResponseEntity.badRequest().body(erros);
	}
	
}

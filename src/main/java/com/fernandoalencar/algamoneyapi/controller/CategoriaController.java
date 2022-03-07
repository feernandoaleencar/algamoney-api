package com.fernandoalencar.algamoneyapi.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandoalencar.algamoneyapi.event.RecursoCriadoEvent;
import com.fernandoalencar.algamoneyapi.model.Categoria;
import com.fernandoalencar.algamoneyapi.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and hasAuthority('SCOPE_write')")
	public ResponseEntity<Categoria> cadastrar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')" )
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')")
	public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		return categoria.isPresent() ?  ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();

	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and hasAuthority('SCOPE_write')")
	public ResponseEntity<Categoria> atualizar(@Valid @PathVariable Long id, @RequestBody Categoria categoria){
		
		if (!categoriaRepository.existsById(id)) {
			return ResponseEntity.notFound().build(); 
		}
		
		categoria.setId(id);
		categoriaRepository.save(categoria);
		
		return ResponseEntity.ok(categoria);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA') and hasAuthority('SCOPE_write')")
	public ResponseEntity<Void> excluir(@PathVariable Long id){
		
		 if (!categoriaRepository.existsById(id)) {
			return ResponseEntity.notFound().build();		
		 }
		 
		 categoriaRepository.deleteById(id);
		 
		 return ResponseEntity.noContent().build();
	}
	
}

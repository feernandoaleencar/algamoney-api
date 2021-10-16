package com.fernandoalencar.algamoneyapi.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fernandoalencar.algamoneyapi.model.Categoria;
import com.fernandoalencar.algamoneyapi.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@PostMapping
	public ResponseEntity<Categoria> cadastrar(@RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(categoriaSalva.getId()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	
	@GetMapping
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		return categoria.isPresent() ?  ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id){
		
		 if (!categoriaRepository.existsById(id)) {
			return ResponseEntity.notFound().build();		
		 }
		 
		 categoriaRepository.deleteById(id);
		 
		 return ResponseEntity.noContent().build();
	}
	
}

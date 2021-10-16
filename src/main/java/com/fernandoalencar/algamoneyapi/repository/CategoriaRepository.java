package com.fernandoalencar.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fernandoalencar.algamoneyapi.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}

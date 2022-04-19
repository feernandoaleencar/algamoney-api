package com.fernandoalencar.algamoneyapi.repository;

import com.fernandoalencar.algamoneyapi.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    List<Municipio> findByEstadoCodigo(Long estadoCodigo);

}

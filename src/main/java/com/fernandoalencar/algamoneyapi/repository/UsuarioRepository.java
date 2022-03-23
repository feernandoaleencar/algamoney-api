package com.fernandoalencar.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fernandoalencar.algamoneyapi.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByEmail(String email);

    public List<Usuario> findByPermissoesDescricao(String permissoesDescricao);
}

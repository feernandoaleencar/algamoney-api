package com.fernandoalencar.algamoneyapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.fernandoalencar.algamoneyapi.model.Pessoa;
import com.fernandoalencar.algamoneyapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContatos().forEach(contato -> contato.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}

	public Pessoa atualizar(Long id, Pessoa pessoa) {

		Pessoa pessoaSalva = buscarPessoaPeloCodigo(id);

		pessoa.getContatos().forEach(contato -> contato.setPessoa(pessoa));

		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");

		return pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		Pessoa pessoaAtualizada = buscarPessoaPeloCodigo(id);
		pessoaAtualizada.setAtivo(ativo);
		
		pessoaRepository.save(pessoaAtualizada);
	}

	private Pessoa buscarPessoaPeloCodigo(Long id) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaSalva;
	}
}

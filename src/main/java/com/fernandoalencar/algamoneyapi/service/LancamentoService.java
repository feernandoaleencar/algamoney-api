package com.fernandoalencar.algamoneyapi.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import com.fernandoalencar.algamoneyapi.dto.LancamentoEstatisticaPessoa;
import com.fernandoalencar.algamoneyapi.mail.Mailer;
import com.fernandoalencar.algamoneyapi.model.Usuario;
import com.fernandoalencar.algamoneyapi.repository.UsuarioRepository;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.model.Pessoa;
import com.fernandoalencar.algamoneyapi.repository.LancamentoRepository;
import com.fernandoalencar.algamoneyapi.repository.PessoaRepository;
import com.fernandoalencar.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    @Autowired
    private Mailer mailer;

    public Lancamento salvar(Lancamento lancamento) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());

        if (!pessoa.isPresent() || !pessoa.get().getAtivo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long id, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(id);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Optional<Pessoa> pessoa = null;
        if (lancamento.getPessoa().getId() != null) {
            pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());
        }

        if (pessoa.isEmpty() || pessoa.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long codigo) {
        /*
        Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (lancamentoSalvo.isEmpty()) {
			throw new IllegalArgumentException();
		}
		*/
        return lancamentoRepository.findById(codigo).orElseThrow(() -> new IllegalArgumentException());
    }

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception{
        List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt","BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    //@Scheduled(cron = "* 16 20 * * *", zone = "America/Sao_Paulo")
    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void avisarSobreLancamentosVencidos(){
        List<Lancamento> vencidos = lancamentoRepository.findbyDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
    }
}

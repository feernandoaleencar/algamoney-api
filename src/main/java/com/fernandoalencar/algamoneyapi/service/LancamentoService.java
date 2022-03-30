package com.fernandoalencar.algamoneyapi.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import com.fernandoalencar.algamoneyapi.dto.LancamentoEstatisticaPessoa;
import com.fernandoalencar.algamoneyapi.mail.Mailer;
import com.fernandoalencar.algamoneyapi.model.Usuario;
import com.fernandoalencar.algamoneyapi.repository.UsuarioRepository;
import com.fernandoalencar.algamoneyapi.storage.S3;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.model.Pessoa;
import com.fernandoalencar.algamoneyapi.repository.LancamentoRepository;
import com.fernandoalencar.algamoneyapi.repository.PessoaRepository;
import com.fernandoalencar.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.util.StringUtils;

@Service
public class LancamentoService {

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Mailer mailer;

    @Autowired
    private S3 s3;

    public Lancamento salvar(Lancamento lancamento) {
        validarPessoa(lancamento);

        if (StringUtils.hasText(lancamento.getAnexo())){
            s3.salvar(lancamento.getAnexo());
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

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    //@Scheduled(fixedDelay = 1000 * 60 * 30)
    @Scheduled(cron = "0 0 6 * * *", zone = "America/Sao_Paulo")
    public void avisarSobreLancamentosVencidos() {
        if (logger.isDebugEnabled()) {
            logger.debug("Preparando envio de e-mail de aviso de lançamento vencidos.");
        }

        List<Lancamento> vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        if (vencidos.isEmpty()) {
            logger.info("Sem lançamentos vencidos.");

            return;
        }

        logger.info("Existem {} lançamentos vencidos.", vencidos.size());

        List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

        if (destinatarios.isEmpty()) {
            logger.warn("Exitem lançamentos vencidos, mas o sistema não encontrou destinatários.");

            return;
        }

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

        logger.info("Lancamentos vencidos enviado por email com sucesso.");
    }
}

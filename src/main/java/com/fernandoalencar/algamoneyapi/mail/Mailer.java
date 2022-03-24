package com.fernandoalencar.algamoneyapi.mail;

import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.model.Usuario;
import com.fernandoalencar.algamoneyapi.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    /*
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @EventListener
    private void teste(ApplicationReadyEvent event){
        String templete = "mail/aviso-lancamentos-vencidos";

        List<Lancamento> lista = lancamentoRepository.findAll();

        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("lancamentos", lista);

        this.enviarEmail("fernandoalencarcontato@gmail.com",
                Arrays.asList("feernando_aleencar@hotmail.com"),
                "Testando",
                templete,
                variaveis);

        System.out.println("Fim do processo de envio de e-mail.");
    }*/

    public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String templete, Map<String, Object> variaveis) {
        Context context = new Context(new Locale("pt", "BR"));

        variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

        String mensagem = thymeleaf.process(templete, context);

        this.enviarEmail(remetente, destinatarios, assunto, mensagem);
    }

    public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Problemas com o envio de e-mail!", e);
        }

    }

    public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios){
        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("lancamentos", vencidos);

        List<String> emails = destinatarios.stream().map(usuario -> usuario.getEmail()).collect(Collectors.toList());

        this.enviarEmail("fernandoalencarcontato@gmail.com", emails, "Lançamentos Vencidos", "mail/aviso-lancamentos-vencidos", variaveis);
    }
}

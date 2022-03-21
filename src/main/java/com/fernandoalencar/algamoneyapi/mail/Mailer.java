package com.fernandoalencar.algamoneyapi.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    /*
    @EventListener
    private void teste(ApplicationReadyEvent event){

        this.enviarEmail("fernandoalencarcontato@gmail.com",
                Arrays.asList("feernando_aleencar@hotmail.com"),
                "Testando",
                "Olá! <br/> Teste de envio de e-mail.");

        System.out.println("Fim do processo de envio de e-mail.");
    }*/

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
}

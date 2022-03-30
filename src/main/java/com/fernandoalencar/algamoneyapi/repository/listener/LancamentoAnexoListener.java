package com.fernandoalencar.algamoneyapi.repository.listener;

import com.fernandoalencar.algamoneyapi.AlgamoneyApiApplication;
import com.fernandoalencar.algamoneyapi.model.Lancamento;
import com.fernandoalencar.algamoneyapi.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class LancamentoAnexoListener {

    @PostLoad
    public void postLoad(Lancamento lancamento){
        if (StringUtils.hasText(lancamento.getAnexo())){
            S3 s3 = AlgamoneyApiApplication.getBean(S3.class);
            lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
        }
    }
}

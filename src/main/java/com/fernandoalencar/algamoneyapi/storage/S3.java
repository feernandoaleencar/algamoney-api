package com.fernandoalencar.algamoneyapi.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fernandoalencar.algamoneyapi.config.property.AlgamoneyApiProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private AlgamoneyApiProperty property;

    @Autowired
    private AmazonS3 amazonS3;

    public String salvarTemporariamente(MultipartFile arquivo) {
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(arquivo.getContentType());
        objectMetadata.setContentLength(arquivo.getSize());

        String nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                property.getS3().getBucket(),
                nomeUnico,
                arquivo.getInputStream(),
                objectMetadata)
                    .withAccessControlList(acl);

            putObjectRequest.setTagging(new ObjectTagging(
                    Arrays.asList(new Tag("expirar", "true"))));

            amazonS3.putObject(putObjectRequest);

            if (logger.isDebugEnabled()){
                logger.debug("Arquivo {} enviado com sucesso para o S3.", arquivo.getOriginalFilename());
            }

            return nomeUnico;
        } catch (IOException e) {
            throw new RuntimeException("Problema ao tentar enviar o arquivo para o S3.", e);
        }
    }

    public String configurarUrl(String objeto) {
        return "\\\\" + property.getS3().getBucket() + ".s3.amazonaws.com/" + objeto;
    }

    public void salvar(String objeto) {
        SetObjectTaggingRequest request = new SetObjectTaggingRequest(
                property.getS3().getBucket(),
                objeto,
                new ObjectTagging(Collections.emptyList())
        );

        amazonS3.setObjectTagging(request);
    }

    private String gerarNomeUnico(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}

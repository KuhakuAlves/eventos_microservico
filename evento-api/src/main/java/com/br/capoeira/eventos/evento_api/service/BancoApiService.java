package com.br.capoeira.eventos.evento_api.service;

import com.br.capoeira.eventos.evento_api.model.Evento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BancoApiService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Value("banco.api.base.uri")
    private String baseUri;

    @Value("banco.api.get.id.uri")
    private String getIdUri;

    @Value("banco.api.get.all.uri")
    private String getAllUri;

    public BancoApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public List<Evento> buscaTodosEventos(){
        //String uri = baseUri.concat(getAllUri);
        String uri = "http://localhost:8181/api/v1/evento/base/dados/all/";
        ResponseEntity<Evento[]> eventosArray = restTemplate.getForEntity(uri, Evento[].class);
        return (eventosArray.getBody() != null)
                ? Arrays.asList(eventosArray.getBody())
                : new ArrayList<>();
    }
}

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

    //@Value("banco.api.base.uri")
    private final String baseUri = "http://localhost:8181/api/v1/evento/base/dados/";

    @Value("banco.api.get.id.uri")
    private String getIdUri;

    //@Value("banco.api.get.all.uri")
    private String getAllUri = "all/";

    public BancoApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public List<Evento> buscaTodosEventos(){
        String uri = baseUri.concat(getAllUri);
        //String uri = "http://localhost:8181/api/v1/evento/base/dados/all/";
        ResponseEntity<Evento[]> eventosArray = restTemplate.getForEntity(uri, Evento[].class);
        return (eventosArray.getBody() != null)
                ? Arrays.asList(eventosArray.getBody())
                : new ArrayList<>();
    }

    public Evento buscarEventoPorId(Long id){
        String uri = baseUri.concat("?id=").concat(id.toString());
        ResponseEntity<Evento> eventoResponse = restTemplate.getForEntity(uri, Evento.class);
        return (eventoResponse.getBody() != null)
                ? eventoResponse.getBody()
                : new Evento();
    }

    public Evento criarEvento(Evento evento){
        ResponseEntity<Evento> eventoResponse = restTemplate.postForEntity(baseUri, evento, Evento.class);
        return (eventoResponse.getBody() != null)
                ? eventoResponse.getBody()
                : new Evento();
    }
}

package com.br.capoeira.eventos.banco_api.service;

import com.br.capoeira.eventos.banco_api.entities.Evento;
import com.br.capoeira.eventos.banco_api.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DadosService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EventoRepository eventoRepository;

    public DadosService(EventoRepository eventoRepository){
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> buscaTodosEventos(){
        logger.info("Buscar todos eventos");
        return eventoRepository.findAll();
    }

    public Evento buscaEventoPorId(Long id){
        logger.info("Buscar evento pelo id {}", id);
        return eventoRepository.findById(id).orElse(null);
    }

    public Evento criarEvento(Evento evento) {
        logger.info("Persistindo um novo evento");
        return eventoRepository.save(evento);
    }
}

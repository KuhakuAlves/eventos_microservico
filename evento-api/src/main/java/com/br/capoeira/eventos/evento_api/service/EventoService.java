package com.br.capoeira.eventos.evento_api.service;

import com.br.capoeira.eventos.evento_api.model.Evento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    private final RabbitTemplate rabbitTemplate;
    private final BancoApiService bancoApiService;

    public EventoService(RabbitTemplate rabbitTemplate, BancoApiService bancoApiService) {
        this.rabbitTemplate = rabbitTemplate;
        this.bancoApiService = bancoApiService;
    }

    public boolean enviaEventoFila(Evento evento) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, "", evento);
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao tentar enfileirar o evento {}, \n mensagem original: {} ", evento, e.getMessage());
            return false;
        }
        logger.info("enviado evento {} para a fila ", evento);
        return true;
    }

    public List<Evento> buscaTodosEventos(){
        return bancoApiService.buscaTodosEventos();
    }
}

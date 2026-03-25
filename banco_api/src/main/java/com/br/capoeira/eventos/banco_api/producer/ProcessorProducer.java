package com.br.capoeira.eventos.banco_api.producer;

import com.br.capoeira.eventos.banco_api.entities.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessorProducer {

    @Value("${rabbitmq.exchange.create.name}")
    private String createExchange;

    @Value("${rabbitmq.exchange.create.name}")
    private String errorCreateExchange;

    @Value("${rabbitmq.exchange.get-all.name}")
    private String getAllExchange;

    private final RabbitTemplate rabbitTemplate;

    public void sendEventForSuccessQueue(Event event){
        rabbitTemplate.convertAndSend(createExchange, "", event);
        log.info("Event sent to : {} successfuly", createExchange);
    }

    public void sendEventForFailQueue(Event event){
        rabbitTemplate.convertAndSend(errorCreateExchange, "", event);
        log.info("Error Event sent to : {} successfuly", errorCreateExchange);
    }

    public void sendEventForGetAllQueue(List<Event> event){
        rabbitTemplate.convertAndSend(getAllExchange, "", event);
        log.info("Sending All Events : {} for client", getAllExchange);
    }
}

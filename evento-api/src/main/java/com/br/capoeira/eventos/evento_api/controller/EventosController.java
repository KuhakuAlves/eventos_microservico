package com.br.capoeira.eventos.evento_api.controller;

import com.br.capoeira.eventos.evento_api.dto.EventoDto;
import com.br.capoeira.eventos.evento_api.mapper.EventoMapper;
import com.br.capoeira.eventos.evento_api.model.Evento;
import com.br.capoeira.eventos.evento_api.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/evento")
public class EventosController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EventoMapper eventoMapper;
    private final EventoService eventoService;

    public EventosController(EventoMapper eventoMapper, EventoService eventoService){
        this.eventoMapper = eventoMapper;
        this.eventoService = eventoService;
    }

    @GetMapping("/all")
    @Operation(summary = "Retorna uma lista de Eventos", description = "Responsavel por retornar todos os eventos na base de dados",
            responses = @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Evento.class)))
    )
    public ResponseEntity<List<Evento>> buscaTodosEventos(){

        return ResponseEntity.ok(eventoService.buscaTodosEventos());
    }
    @PostMapping("/cadastro")
    @Operation(summary = "Cadastra um novo evento", description = "Contem a operação para criar um novo evento",
            responses = @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    )
    public ResponseEntity<String> criarEvento(@RequestBody EventoDto eventoDto){
        logger.info("Evento recebido, {}", eventoDto);
        Evento evento = eventoMapper.eventoDtoToEvento(eventoDto);
        eventoService.enviaEventoFila(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body("Evento criado com sucesso!!");
    }
}

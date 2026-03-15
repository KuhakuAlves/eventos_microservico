package com.br.capoeira.eventos.banco_api.controller;

import com.br.capoeira.eventos.banco_api.entities.Evento;
import com.br.capoeira.eventos.banco_api.service.DadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/evento/base/dados/")
public class DadosController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DadosService dadosService;

    public DadosController(DadosService dadosService){
        this.dadosService = dadosService;
    }

    @GetMapping("all/")
    @Operation(summary = "Retorna uma lista de Eventos", description = "Responsavel por retornar todos os eventos na base de dados",
            responses = @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Evento.class)))
    )
    public ResponseEntity<List<Evento>> retornaTodosEventos(){
        List<Evento> eventos = dadosService.buscaTodosEventos();
        logger.info("Retornando lista de eventos {}", eventos);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping()
    @Operation(summary = "Retorna um Evento", description = "Responsavel por retornar um evento, pesquisando pelo id",
            responses = @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Evento.class)))
    )
    public ResponseEntity<Evento> retornaEventoPorId(@RequestParam("id") Long id){
        Evento evento = dadosService.buscaEventoPorId(id);
        logger.info("Retornando evento {}", evento);
        return ResponseEntity.ok(evento);
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo evento", description = "Contem a operação para criar um novo evento",
            responses = @ApiResponse(responseCode = "201", description = "Evento criado com sucesso!!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Evento.class)))
    )
    public  ResponseEntity<Evento> criaNovoEvento(@RequestBody Evento evento){
        Evento newEvento = dadosService.criarEvento(evento);
        logger.info("Evento criado {}", newEvento);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvento);
    }
}

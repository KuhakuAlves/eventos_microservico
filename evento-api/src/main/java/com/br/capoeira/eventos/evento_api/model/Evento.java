package com.br.capoeira.eventos.evento_api.model;

import com.br.capoeira.eventos.evento_api.enums.TipoContato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {
    private String titulo;
    private String descricao;
    private LocalDateTime dataHora;
    private String endereco;
    private TipoContato tipoContato;
    private String contato;
    private String imagem;
}

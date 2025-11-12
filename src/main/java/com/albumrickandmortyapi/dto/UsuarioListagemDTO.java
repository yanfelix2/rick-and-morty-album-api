package com.albumrickandmortyapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// Esse DTO serve para transferir dados básicos do usuário, incluindo o ID do álbum associado.
@Data // Gera getters e setters automaticamente
@NoArgsConstructor // Gera um construtor sem argumentos
public class UsuarioListagemDTO {

    private Long id;
    private String nome;
    private String email;
    private Long idAlbum;

}

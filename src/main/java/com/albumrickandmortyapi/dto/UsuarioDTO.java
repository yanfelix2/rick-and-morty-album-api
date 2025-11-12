package com.albumrickandmortyapi.dto;


import lombok.Data;


// Esse DTO serve para transferir dados básicos do usuário, e colocar uma camada de abstração entre a entidade Usuario e o que é exposto via API.
@Data // Gera getters e setters automaticamente
public class UsuarioDTO {
    private String nome;
    private String email;
}

package com.albumrickandmortyapi.dto;

import lombok.Data;


// Esse DTO serve para transferir dados de personagens obtidos da API do Rick and Morty.
@Data  // Essa anotação do Lombok gera automaticamente os getters, setters, toString, equals
public class CharacterDTO {
    private Long id;
    private String name;
    private String status; // Usado para a logica de raridade, quando o personagem esta morto ou vivo, por exemplo.
    private String species;
}

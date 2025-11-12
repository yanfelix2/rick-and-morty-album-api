package com.albumrickandmortyapi.dto;

import lombok.Data;


// Esse DTO serve para mapear a resposta raiz da API de personagens do Rick and Morty.
@Data // Essa anotação do Lombok gera automaticamente os getters, setters, toString, equals
public class RootCharacterResponseDTO {

    // Campo info da resposta da API, que contém metadados sobre a resposta de personagens da API. ( Metadados seria informações adicionais sobre a resposta da API, como o total de personagens, número de páginas, etc.)
    private Info info;

    @Data // Essa anotação do Lombok gera automaticamente os getters, setters, toString, equals
    // Classe interna para representar os metadados da resposta da API.
    public static class Info {
        private int count; // Total de personagens
        private int pages; // Número total de páginas
    }


}

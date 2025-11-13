package com.albumrickandmortyapi.dto;

import lombok.Data;

// Esse DTO serve para transferir dados de uma proposta de troca entre usuários.
@Data // Gera getters e setters automaticamente
public class PropostaTrocaDTO {
    private Long ofertanteId; // Quem está propondo
    private Long figurinhaOferecidaId; // ID da figurinha que ele vai dar
    private Long receptorId; // Quem vai receber a proposta
    private Long figurinhaDesejadaId; // ID da figurinha que ele quer receber

    // Aqui não é preciso do metodo FromEntity pq é um DTO DE entrada
}
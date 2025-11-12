package com.albumrickandmortyapi.dto;

import com.albumrickandmortyapi.model.PropostaTroca;
import lombok.Builder;
import lombok.Data;


// Esse DTO serve para transferir dados detalhados de uma proposta de troca entre usuários.
@Data // Gera getters, setters, toString, equals e hashCode
@Builder // Facilita a construção de objetos dessa classe
public class DetalheTrocaDTO {
    private Long id;
    private String status;

    // Informações do Ofertante
    private Long idOfertante;
    private String nomeOfertante;

    // Informações do Receptor
    private Long idReceptor;
    private String nomeReceptor;

    // Figurinhas Envolvidas
    private Long idFigurinhaOferecida;
    private String nomeFigurinhaOferecida;
    private String raridadeOferecida; // Adicionei a raridade para mais contexto

    private Long idFigurinhaDesejada;
    private String nomeFigurinhaDesejada;
    private String raridadeDesejada; // Adicionei a raridade para mais contexto

    // Construtor estático para mapeamento da entidade
    // Aqui serve para converter uma entidade PropostaTroca em um DetalheTrocaDTO
    public static DetalheTrocaDTO fromEntity(PropostaTroca proposta) {
        return DetalheTrocaDTO.builder()
                .id(proposta.getId())
                .status(proposta.getStatus())

                // Ofertante
                .idOfertante(proposta.getUsuarioOfertante().getId())
                .nomeOfertante(proposta.getUsuarioOfertante().getNome())

                // Receptor
                .idReceptor(proposta.getUsuarioReceptor().getId())
                .nomeReceptor(proposta.getUsuarioReceptor().getNome())

                // Oferecida
                .idFigurinhaOferecida(proposta.getFigurinhaOferecida().getId())
                .nomeFigurinhaOferecida(proposta.getFigurinhaOferecida().getNomePersonagem())
                .raridadeOferecida(proposta.getFigurinhaOferecida().getRaridade().toString())
                // Desejada
                .idFigurinhaDesejada(proposta.getFigurinhaDesejada().getId())
                .nomeFigurinhaDesejada(proposta.getFigurinhaDesejada().getNomePersonagem())
                .raridadeDesejada(proposta.getFigurinhaDesejada().getRaridade().toString())

                // O build serve para finalizar a construção do objeto
                .build();
    }
}
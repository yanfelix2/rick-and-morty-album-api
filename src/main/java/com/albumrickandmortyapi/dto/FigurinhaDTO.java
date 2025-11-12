package com.albumrickandmortyapi.dto;

import com.albumrickandmortyapi.model.Figurinha;
import lombok.Builder;
import lombok.Data;

// Esse DTO serve para transferir dados de figurinhas do álbum.
@Data // Essa anotação do Lombok gera automaticamente os getters, setters, toString, equals e hashCode
@Builder // Esse builder serve para facilitar a construção de objetos dessa classe, permitindo a criação de instâncias de forma mais legível e flexível.
public class FigurinhaDTO {


    private Long idFigurinha;
    private Long idPersonagemApi;
    private String nomePersonagem;
    private String raridade;

    // Aqui serve para converter uma entidade Figurinha em um FigurinhaDTO
    public static FigurinhaDTO fromEntity(Figurinha figurinha) {
        return FigurinhaDTO.builder()
                .idFigurinha(figurinha.getId())
                .idPersonagemApi(figurinha.getIdPersonagemApi())
                .nomePersonagem(figurinha.getNomePersonagem())

                // **CORREÇÃO AQUI:** Chama .toString() no Enum
                .raridade(figurinha.getRaridade().toString())

                .build();
    }
}
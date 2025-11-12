package com.albumrickandmortyapi.dto;


import com.albumrickandmortyapi.model.Figurinha;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

// Esse DTO serve para transferir dados detalhados do álbum de um usuário, incluindo o progresso e as figurinhas que ele possui.
@Data // Essa anotação do Lombok gera automaticamente os getters, setters, toString, equals e hashCode
@Builder // Esse builder serve para facilitar a construção de objetos dessa classe, permitindo a criação de instâncias de forma mais legível e flexível.
public class AlbumDetalhadoDTO {


    private double progressoPercentual;

    // Lista de todas as figurinhas que o usuário possui (incluindo repetidas, como na entidade Album)
    private List<Figurinha> figurinhasBrutas;

    // Repetidas consolidadas: Mapeia o nome da figurinha para a contagem de duplicatas
    // Ex: {"Rick Sanchez - COMUM": 3, "Morty Smith - RARA": 1}
    private Map<String, Integer> contagemDeRepetidas;
}
package com.albumrickandmortyapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que essa classe é uma entidade JPA que será mapeada para uma tabela no banco de dados.
@Data// Gera automaticamente os getters, setters, toString, equals e hashCode
@NoArgsConstructor //  Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos
@Table(name = "figurinhas") // Especifica o nome da tabela no banco de dados para essa entidade.
public class Figurinha {

    @Id // Indica que esse campo é a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que o valor da chave primária será gerado automaticamente pelo banco de dados.
    private Long id; // Campo que representa o ID único da figurinha.


    private Long idPersonagemApi; // Campo que representa o ID do personagem na API do Rick and Morty.

    private String nomePersonagem; // Campo que representa o nome do personagem.

//    private String raridade; // COMUM, RARA, LENDARIA, ESPECIAL

    @Enumerated(EnumType.STRING)
    private Raridade raridade;

    @ManyToOne(fetch = FetchType.LAZY) // Define um relacionamento muitos-para-um entre Figurinha e Album.
    @JoinColumn(name = "album_id") // Especifica a coluna que será usada para o join com a tabela Album.
    @JsonBackReference("album-figurinhas") // Evita referência cíclica durante a serialização JSON.
    private Album album; // Campo que representa o álbum ao qual a figurinha pertence.
}

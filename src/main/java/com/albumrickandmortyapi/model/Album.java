package com.albumrickandmortyapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity // Indica que essa classe é uma entidade JPA que será mapeada para uma tabela no banco de dados.
@Data // Gera automaticamente os getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos
@Table(name = "albums") // Especifica o nome da tabela no banco de dados para essa entidade.
public class Album {

    @Id // Indica que esse campo é a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que o valor da chave primária será gerado automaticamente pelo banco de dados.
    private Long id; // Campo que representa o ID único do álbum.

    @OneToOne(fetch = FetchType.LAZY) // Define um relacionamento um-para-um entre Album e Usuario.
    @JoinColumn(name = "usuario_id") // Especifica a coluna que será usada para o join com a tabela Usuario.
    @JsonBackReference // Evita referência cíclica durante a serialização JSON.
    private Usuario usuario; // Campo que representa o usuário dono do álbum.

    // mappedBy aponta para o campo 'album' na classe Figurinha
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true) // Define um relacionamento um-para-muitos entre Album e Figurinha.
    @JsonManagedReference("album-figurinhas") // Evita referência cíclica durante a serialização JSON.
    private List<Figurinha> figurinhas = new ArrayList<>(); // Lista de figurinhas associadas ao álbum.
}

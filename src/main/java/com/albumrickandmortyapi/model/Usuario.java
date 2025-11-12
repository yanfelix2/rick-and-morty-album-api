package com.albumrickandmortyapi.model;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que essa classe é uma entidade JPA
@Data // Gera getters e setters automaticamente
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos
@Table(name = "usuarios") // Define o nome da tabela no banco de dados
public class Usuario {


    @Id // Indica o campo de chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o valor automaticamente
    private Long id; // ID do usuário

    private String nome; // Nome do usuário

    @Column(unique = true) // Garante que o email seja único no banco de dados
    private String email; // Email do usuário

    // O album é criado junto com o usuário (CascadeType.ALL)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // Relacionamento um-para-um com Album
    @JsonManagedReference // Evita referência cíclica na serialização JSON ( a referencia ciclica é um tipo de loop infinito que ocorre quando dois objetos se referenciam mutuamente durante a serialização para JSON)
    private Album album; // Álbum associado ao usuário
}

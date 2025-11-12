package com.albumrickandmortyapi.dto;

import com.albumrickandmortyapi.model.Usuario;
import lombok.Builder;
import lombok.Data;

// DTO para transferir dados detalhados do usuário, incluindo o ID do álbum associado.
@Data // Gera getters, setters, toString, equals e hashCode automaticamente
@Builder // Facilita a construção de objetos dessa classe
public class UsuarioDetalheDTO {

    private Long id;
    private String nome;
    private String email;
    private Long idAlbum;

    // Construtor estático para mapeamento
    // Isso daqui faz o mapeamento da entidade Usuario para o DTO UsuarioDetalheDTO, ou seja pega os dados do Usuario( entidade ) e coloca no DTO
    public static UsuarioDetalheDTO fromEntity(Usuario usuario) {
        Long albumId = (usuario.getAlbum() != null) ? usuario.getAlbum().getId() : null;

        return UsuarioDetalheDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .idAlbum(albumId)
                .build();
    }
}
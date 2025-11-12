package com.albumrickandmortyapi.repository;

import com.albumrickandmortyapi.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositório para a entidade Album, estendendo JpaRepository para fornecer operações CRUD básicas.
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByUsuarioId(Long usuarioId);
}

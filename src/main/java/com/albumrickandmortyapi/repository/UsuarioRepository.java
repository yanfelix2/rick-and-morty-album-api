package com.albumrickandmortyapi.repository;

import com.albumrickandmortyapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para a entidade Usuario, estendendo JpaRepository para fornecer operações CRUD básicas.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

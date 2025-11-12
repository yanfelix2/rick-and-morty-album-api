package com.albumrickandmortyapi.repository;

import com.albumrickandmortyapi.model.Figurinha;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para a entidade Figurinha, estendendo JpaRepository para fornecer operações CRUD básicas.
public interface FigurinhaRepository extends JpaRepository<Figurinha, Long> {
}

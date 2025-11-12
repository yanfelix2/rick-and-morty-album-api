package com.albumrickandmortyapi.repository;

import com.albumrickandmortyapi.model.PropostaTroca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositório para a entidade PropostaTroca, estendendo JpaRepository para fornecer operações CRUD básicas.
public interface PropostaTrocaRepository extends JpaRepository<PropostaTroca, Long> {
    // Busca todas as propostas enviadas ou recebidas por um usuário
//    List<PropostaTroca> findByUsuarioOfertanteIdOrUsuarioReceptorId(Long ofertanteId, Long receptorId);
}
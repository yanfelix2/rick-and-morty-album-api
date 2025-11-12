package com.albumrickandmortyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PropostaTroca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lado Ofertante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ofertante_id")
    // ATENÇÃO AQUI: Ignorar os campos do Hibernate Proxy e o relacionamento cíclico 'album'
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "album"})
    private Usuario usuarioOfertante;

    // Lado Receptor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id")
    // ATENÇÃO AQUI: Ignorar os campos do Hibernate Proxy e o relacionamento cíclico 'album'
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "album"})
    private Usuario usuarioReceptor;

    // Figurinha oferecida
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figurinha_oferecida_id")
    // ATENÇÃO AQUI: Ignorar os campos do Hibernate Proxy e o relacionamento cíclico 'album'
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "album"})
    private Figurinha figurinhaOferecida;

    // Figurinha desejada
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figurinha_desejada_id")
    // ATENÇÃO AQUI: Ignorar os campos do Hibernate Proxy e o relacionamento cíclico 'album'
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "album"})
    private Figurinha figurinhaDesejada;

    private String status = "PENDENTE";
}
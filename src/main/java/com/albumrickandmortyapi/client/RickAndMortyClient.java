package com.albumrickandmortyapi.client;

import com.albumrickandmortyapi.dto.CharacterDTO;
import com.albumrickandmortyapi.dto.RootCharacterResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Essa interface serve para fazer chamadas à API externa do Rick and Morty, utilizando o FeignClient do Spring Cloud OpenFeign.
// Sendo assim o objetivo dela é facilitar a comunicação com a API externa, abstraindo os detalhes das requisições HTTP.

@FeignClient(name = "rick-and-morty-api", url = "https://rickandmortyapi.com/api")
public interface RickAndMortyClient {

    // Retorna um personagem específico
    @GetMapping("/character/{id}")
    CharacterDTO buscarPersonagemPorId(@PathVariable("id") Long id);

    // Retorna a lista completa (usado para obter o total de personagens)
    @GetMapping("/character")
    RootCharacterResponseDTO buscarDadosGerais();
}

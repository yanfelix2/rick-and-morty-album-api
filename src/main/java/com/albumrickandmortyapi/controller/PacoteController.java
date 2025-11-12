package com.albumrickandmortyapi.controller;

import com.albumrickandmortyapi.dto.FigurinhaDTO;
import com.albumrickandmortyapi.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Indica que esta classe é um controller REST
@Tag(name = "Pacotes", description = "Endpoints para gerenciar pacotes de figurinhas") // Documentação Swagger
@RequestMapping("/pacotes") // Rota base para os endpoints de pacotes
public class PacoteController {

    // Aqui faz injeção de dependência do serviço de álbum
    private final AlbumService albumService;

    public PacoteController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // Essa rota serve para abrir um pacote de figurinhas para um usuário específico, passando o ID do usuário como parâmetro na URL.
    // Aqui é uma logica que simula a abertura de um pacote de figurinhas, retornando uma lista de figurinhas novas para o usuário.
    @Operation(description = "Abrir um pacote de figurinhas para um usuário específico", summary = "Abre o pacote de figurinhas de um usuário")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Pacote aberto com sucesso, figurinhas retornadas"),
            @ApiResponse (responseCode = "400", description = "Erro ao abrir o pacote de figurinhas")
    })
    @Parameter(name = "usuarioId", description = "ID do usuário que está abrindo o pacote de figurinhas", required = true)
    @PostMapping("/abrir/{usuarioId}")
    public ResponseEntity<List<FigurinhaDTO>> abrirPacote(@PathVariable Long usuarioId) {
        // Aqui no try, é chamado o serviço para abrir o pacote e retornamos as figurinhas novas. Se não der certo, retorna um erro 400 com a mensagem de erro.
        try {
            List<FigurinhaDTO> figurinhasNovas = albumService.abrirPacote(usuarioId);
            return ResponseEntity.ok(figurinhasNovas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().header("Error-Message", e.getMessage()).build();
        }
    }
}
package com.albumrickandmortyapi.controller;

import com.albumrickandmortyapi.dto.AlbumDetalhadoDTO;
import com.albumrickandmortyapi.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Essa classe serve para gerenciar as requisições relacionadas aos álbuns de personagens do Rick and Morty.
// Ela define endpoints para visualizar detalhes do álbum e calcular o progresso do álbum para um usuário específico

@RestController // Indica que esta classe é um controller REST
@Tag(name = "Álbuns", description = "Endpoints para gerenciar álbuns dos usuarios") // Documentação Swagger
@RequestMapping("/albuns") // Rota base para os endpoints de álbuns
public class AlbumController {

    // Aqui faz a injeção de dependência do serviço de álbum
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // Essa rota serve para visualizar os detalhes do álbum de um usuário específico, passando o ID do usuário como parâmetro na URL.
    @Operation(description = "Visualizar detalhes do álbum de um usuário específico", summary = "Retorna os detalhes do álbum")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Detalhes do álbum retornados com sucesso"),
            @ApiResponse (responseCode = "404", description = "Usuário ou álbum não encontrado")
    })
    @Parameter(name = "usuarioId", description = "ID do usuário cujo álbum será visualizado", required = true)
    @GetMapping("/{usuarioId}")
    public ResponseEntity<AlbumDetalhadoDTO> visualizarAlbumDetalhado(@PathVariable Long usuarioId) {
        try {
            AlbumDetalhadoDTO detalhes = albumService.buscarDetalhesAlbumSimples(usuarioId);
            return ResponseEntity.ok(detalhes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Essa rota serve para calcular o progresso do álbum de um usuário específico, passando o ID do usuário como parâmetro na URL.
    // Ela pega a porcentagem de personagens coletados pelo usuário em relação ao total de personagens disponíveis.
    @Operation(description = "Calcular o progresso do álbum de um usuário específico", summary = "Retorna a porcentagem de progresso do álbum de um usuário")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Progresso do álbum calculado com sucesso"),
            @ApiResponse (responseCode = "404", description = "Usuário ou álbum não encontrado")
    })
    @Parameter(name = "usuarioId", description = "ID do usuário cujo progresso do álbum será calculado", required = true)
    @GetMapping("/{usuarioId}/progresso")
    public ResponseEntity<Double> getProgressoAlbum(@PathVariable Long usuarioId) {
        try {
            double progresso = albumService.calcularPorcentagemCompleta(usuarioId);
            return ResponseEntity.ok(progresso);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
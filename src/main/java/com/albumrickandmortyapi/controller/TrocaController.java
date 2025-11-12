package com.albumrickandmortyapi.controller;

import com.albumrickandmortyapi.dto.DetalheTrocaDTO;
import com.albumrickandmortyapi.dto.PropostaTrocaDTO;
import com.albumrickandmortyapi.service.TrocaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que esta classe é um controller REST
@Tag(name = "Trocas", description = "Endpoints para gerenciar trocas de figurinhas entre usuários") // Documentação Swagger
@RequestMapping("/trocas") // Rota base para os endpoints de trocas
public class TrocaController {

    // Aqui faz a injeção de dependência do serviço de troca
    private final TrocaService trocaService;

    public TrocaController(TrocaService trocaService) {
        this.trocaService = trocaService;
    }



    // Esse endpoint serve para PROPOR uma nova troca, passando os detalhes da proposta no corpo da requisição.
    @Operation(description = "Propor uma nova troca de figurinhas entre usuários", summary = "Cria uma nova proposta de troca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proposta de troca criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar a proposta de troca")
    })
    @Parameter(name = "dto", description = "Detalhes da proposta de troca", required = true)
    @PostMapping("/propor")
    public ResponseEntity<DetalheTrocaDTO> proporTroca(@RequestBody PropostaTrocaDTO dto) {
        try {
            DetalheTrocaDTO detalhes = trocaService.proporTroca(dto);
            // Retorna 201 Created para criação bem-sucedida, caso não haja erros. Se houver erro é retornado 400 Bad Request.
            return new ResponseEntity<>(detalhes, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().header("Error-Message", e.getMessage()).build();
        }
    }


    // Esse endpoint serve para ACEITAR uma troca proposta, passando o ID da proposta na URL e o ID do usuário que está aceitando como parâmetro de consulta.
    @Operation(description = "Aceitar uma proposta de troca de figurinhas", summary = "Aceita uma proposta de troca existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proposta de troca aceita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao aceitar a proposta de troca")
    })
    @Parameter(name = "propostaId", description = "ID da proposta de troca a ser aceita", required = true)
    @Parameter(name = "usuarioId", description = "ID do usuário que está aceitando a proposta de troca", required = true)
    @PutMapping("/aceitar/{propostaId}")
    public ResponseEntity<DetalheTrocaDTO> aceitarTroca(@PathVariable Long propostaId, @RequestParam Long usuarioId) {
        try {
            DetalheTrocaDTO detalhes = trocaService.aceitarTroca(propostaId, usuarioId);
            // Retorna 200 OK
            return ResponseEntity.ok(detalhes); // Caso de errado, entra no catch e retorna 400 Bad Request
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().header("Error-Message", e.getMessage()).build();
        }
    }

    // Esse endpoint serve para DELETAR uma troca proposta, passando o ID da proposta na URL.
    @Operation(description = "Deletar uma proposta de troca de figurinhas", summary = "Deleta uma proposta de troca existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proposta de troca deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Proposta de troca não encontrada")
    })
    @Parameter(name = "propostaId", description = "ID da proposta de troca a ser deletada", required = true)
    @DeleteMapping("/{propostaId}")
    public ResponseEntity<Void> deletarTroca(@PathVariable Long propostaId) {
        try {
            trocaService.deletarTroca(propostaId);
            // Retorna 204 No Content, que é o padrão.
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Pode ser 404 Not Found se a exceção do Service indicar item não encontrado
            return ResponseEntity.notFound().build();
        }
    }


    // Esse endpoint serve para BUSCAR os detalhes de uma troca específica, passando o ID da proposta na URL.
    @Operation(description = "Buscar os detalhes de uma troca específica", summary = "Retorna os detalhes de uma proposta de troca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes da troca retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Proposta de troca não encontrada")
    })
    @Parameter(name = "propostaId", description = "ID da proposta de troca a ser buscada", required = true)
    @GetMapping("/{propostaId}")
    public ResponseEntity<DetalheTrocaDTO> buscarTroca(@PathVariable Long propostaId) {
        try { // Aqui chama o serviço para buscar os detalhes da troca. Se encontrar, retorna 200 OK com os detalhes.
            // Caso não encontre ou ocorra algum erro, entra no catch e retorna 404 Not Found.
            DetalheTrocaDTO detalhes = trocaService.buscarDetalheTrocaPorId(propostaId);
            return ResponseEntity.ok(detalhes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
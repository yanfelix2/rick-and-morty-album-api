package com.albumrickandmortyapi.controller;

import com.albumrickandmortyapi.dto.UsuarioDetalheDTO;
import com.albumrickandmortyapi.dto.UsuarioDTO;
import com.albumrickandmortyapi.dto.UsuarioListagemDTO;
import com.albumrickandmortyapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Indica que esta classe é um controller REST
@Tag(name = "Usuários", description = "Endpoints para gerenciar usuários") // Documentação Swagger
@RequestMapping("/usuarios") // Rota base para os endpoints de usuários
public class UsuarioController {

    // Aqui faz a injeção de dependência do serviço de usuário
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Esse endpoint serve para CRIAR um novo usuário, passando os detalhes do usuário no corpo da requisição.
    @Operation(description = "Criar um novo usuário com álbum associado", summary = "Cria um novo usuário e inicializa seu álbum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar o usuário")
    })
    @Parameter(name = "usuarioDTO", description = "Detalhes do usuário a ser criado", required = true)
    @PostMapping("/criar")
    public ResponseEntity<UsuarioDetalheDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDetalheDTO novoUsuario = usuarioService.criarUsuarioComAlbum(usuarioDTO);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    // Esse endpoint serve para BUSCAR um usuário específico, passando o ID do usuário como parâmetro na URL.
    @Operation(description = "Buscar um usuário específico pelo ID", summary = "Retorna os detalhes de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @Parameter(name = "usuarioId", description = "ID do usuário a ser buscado", required = true)
    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDetalheDTO> buscarUsuario(@PathVariable Long usuarioId) {
        try {
            UsuarioDetalheDTO usuario = usuarioService.buscarUsuarioDetalhePorId(usuarioId);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Esse endpoint serve para EDITAR um usuário específico, passando o ID do usuário na URL e os novos detalhes no corpo da requisição.
    @Operation(description = "Editar um usuário específico pelo ID", summary = "Atualiza os detalhes de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @Parameter(name = "usuarioId", description = "ID do usuário a ser editado", required = true)
    @PutMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDetalheDTO> editarUsuario(@PathVariable Long usuarioId, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDetalheDTO usuarioAtualizado = usuarioService.editarUsuario(usuarioId, usuarioDTO);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Esse endpoint serve para EXCLUIR um usuário específico, passando o ID do usuário na URL.]
    @Operation(description = "Excluir um usuário específico pelo ID", summary = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @Parameter(name = "usuarioId", description = "ID do usuário a ser excluído", required = true)
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long usuarioId) {
        try {
            usuarioService.excluirUsuario(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Esse endpoint serve para LISTAR todos os usuários cadastrados no sistema.
    @Operation(description = "Listar todos os usuários cadastrados", summary = "Retorna uma lista de todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioListagemDTO>> listarUsuarios() {
        List<UsuarioListagemDTO> usuarios = usuarioService.listarTodosUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }
}
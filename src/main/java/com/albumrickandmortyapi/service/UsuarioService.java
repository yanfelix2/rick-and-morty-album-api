package com.albumrickandmortyapi.service;

import com.albumrickandmortyapi.dto.UsuarioDTO;
import com.albumrickandmortyapi.dto.UsuarioListagemDTO;
import com.albumrickandmortyapi.dto.UsuarioDetalheDTO; // NOVO DTO DE SAÍDA
import com.albumrickandmortyapi.model.Album;
import com.albumrickandmortyapi.model.Usuario;
import com.albumrickandmortyapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indica que esta classe é um serviço do Spring
public class UsuarioService {

    // --- Injeção de Dependências ---
    private final UsuarioRepository usuarioRepository;

    // Construtor para injeção de dependências
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método de busca INTERNA (retorna a ENTIDADE, usado por outros métodos do Service)
    public Usuario buscarUsuarioPorId(Long id) { // Retorna a ENTIDADE
        return usuarioRepository.findById(id) // Busca o usuário pelo ID
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id)); // Lança exceção se não encontrado
    }

    // 1. CRIAR Usuário
    @Transactional // Garante que a operação seja atômica (ou tudo é salvo ou nada é salvo)
    public UsuarioDetalheDTO criarUsuarioComAlbum(UsuarioDTO dto) { // metodo serve para criar um usuário e seu álbum associado
        Usuario usuario = new Usuario(); // Cria a entidade Usuario
        usuario.setNome(dto.getNome()); // Define o nome do usuário
        usuario.setEmail(dto.getEmail()); // Define o email do usuário

        // Cria o Álbum e faz o link bidirecional
        Album album = new Album(); // Cria a entidade Album
        album.setUsuario(usuario); // Define o usuário dono do álbum
        usuario.setAlbum(album); // Define o álbum do usuário

        Usuario usuarioSalvo = usuarioRepository.save(usuario); // Salva o usuário (e o álbum por cascade)

        // Mapeia e retorna o DTO de detalhe
        return UsuarioDetalheDTO.fromEntity(usuarioSalvo); // Retorna o DTO mapeado da entidade salva
    }

    // NOVO MÉTODO: Busca para o Controller (Retorna DTO)
    public UsuarioDetalheDTO buscarUsuarioDetalhePorId(Long id) { // metodo serve para buscar um usuário pelo ID e retornar seu detalhe em DTO
        Usuario usuario = buscarUsuarioPorId(id); // Usa o método do Service para buscar a entidade

        // Mapeia e retorna o DTO de detalhe
        return UsuarioDetalheDTO.fromEntity(usuario);
    }

    // 2. EDITAR Usuário
    @Transactional
    public UsuarioDetalheDTO editarUsuario(Long id, UsuarioDTO dto) { // metodo serve para editar os dados de um usuário existente
        Usuario usuarioExistente = buscarUsuarioPorId(id); // Busca a entidade existente pelo ID

        // Atualiza os campos
        usuarioExistente.setNome(dto.getNome()); // Atualiza o nome do usuário
        usuarioExistente.setEmail(dto.getEmail()); // Atualiza o email do usuário

        // Retorna o DTO mapeado da entidade gerenciada
        return UsuarioDetalheDTO.fromEntity(usuarioExistente); // Retorna o DTO mapeado da entidade atualizada
    }

    // 3. EXCLUIR Usuário
    @Transactional // Garante que a operação seja atômica
    public void excluirUsuario(Long id) { // metodo serve para excluir um usuário pelo ID
        Usuario usuario = buscarUsuarioPorId(id); // Busca a entidade pelo ID
        usuarioRepository.delete(usuario); // Exclui o usuário
    }


    // 4. LISTAGEM de Usuários
    public List<UsuarioListagemDTO> listarTodosUsuarios() { // metodo serve para listar todos os usuários cadastrados
        // Busca todos os usuários
        List<Usuario> usuarios = usuarioRepository.findAll(); // Retorna uma lista de entidades Usuario

        // Mapeia cada entidade Usuario para o UsuarioListagemDTO
        return usuarios.stream() // Esse stream serve para transformar a lista de entidades em uma lista de DTOs
                .map(usuario -> { // Mapeia cada usuário individualmente
                    UsuarioListagemDTO dto = new UsuarioListagemDTO(); // Cria um novo DTO
                    dto.setId(usuario.getId()); // Define o ID do usuário
                    dto.setNome(usuario.getNome()); // Define o nome do usuário
                    dto.setEmail(usuario.getEmail()); // Define o email do usuário

                    // Verifica se o álbum existe antes de pegar o ID
                    if (usuario.getAlbum() != null) { // Verifica se o álbum não é nulo
                        dto.setIdAlbum(usuario.getAlbum().getId()); // Define o ID do álbum associado
                    }
                    return dto; // Retorna o DTO mapeado
                })
                .collect(Collectors.toList()); // Coleta os DTOs em uma lista e retorna
    }
}
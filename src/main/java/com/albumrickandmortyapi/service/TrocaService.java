package com.albumrickandmortyapi.service;

import com.albumrickandmortyapi.dto.DetalheTrocaDTO;
import com.albumrickandmortyapi.dto.PropostaTrocaDTO;
import com.albumrickandmortyapi.model.Album;
import com.albumrickandmortyapi.model.Figurinha;
import com.albumrickandmortyapi.model.PropostaTroca;
import com.albumrickandmortyapi.model.Usuario;
import com.albumrickandmortyapi.repository.FigurinhaRepository;
import com.albumrickandmortyapi.repository.PropostaTrocaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service // Anotação para marcar esta classe como um serviço do Spring
public class TrocaService { // Serviço para gerenciar propostas de troca entre usuários

    // --- Injeção de Dependências ---
    private final PropostaTrocaRepository propostaTrocaRepository; // Repositório para acessar dados de PropostaTroca
    private final UsuarioService usuarioService; // Serviço para gerenciar usuários
    private final FigurinhaRepository figurinhaRepository; // Repositório para acessar dados de Figurinha

    // Construtor para injeção de dependências
    public TrocaService(PropostaTrocaRepository propostaTrocaRepository, UsuarioService usuarioService, FigurinhaRepository figurinhaRepository) {
        this.propostaTrocaRepository = propostaTrocaRepository;
        this.usuarioService = usuarioService;
        this.figurinhaRepository = figurinhaRepository;
    }

    // 1. Criar Proposta (AGORA RETORNA DetalheTrocaDTO)
    @Transactional
    public DetalheTrocaDTO proporTroca(PropostaTrocaDTO dto) { // Retorna DTO
        // Busca entidades envolvidas na proposta
        Usuario ofertante = usuarioService.buscarUsuarioPorId(dto.getOfertanteId());
        Usuario receptor = usuarioService.buscarUsuarioPorId(dto.getReceptorId());

        // Busca as figurinhas envolvidas na proposta
        Figurinha oferecida = figurinhaRepository.findById(dto.getFigurinhaOferecidaId())
                .orElseThrow(() -> new RuntimeException("Figurinha oferecida não encontrada."));

        // Busca a figurinha desejada
        Figurinha desejada = figurinhaRepository.findById(dto.getFigurinhaDesejadaId())
                .orElseThrow(() -> new RuntimeException("Figurinha desejada não encontrada."));

        // Validações básicas, faz sentido garantir que as figurinhas pertencem aos usuários corretos
        if (!oferecida.getAlbum().getId().equals(ofertante.getAlbum().getId())) {
            throw new RuntimeException("Figurinha oferecida não pertence ao ofertante.");
        }
        if (!desejada.getAlbum().getId().equals(receptor.getAlbum().getId())) {
            throw new RuntimeException("Figurinha desejada não pertence ao receptor.");
        }

        PropostaTroca proposta = new PropostaTroca(); // Cria nova entidade PropostaTroca
        proposta.setUsuarioOfertante(ofertante); // Define ofertante
        proposta.setUsuarioReceptor(receptor);// Define receptor
        proposta.setFigurinhaOferecida(oferecida); // Define figurinha oferecida
        proposta.setFigurinhaDesejada(desejada); // Define figurinha desejada
        proposta.setStatus("PENDENTE"); // Define status inicial

        proposta = propostaTrocaRepository.save(proposta); // Salva a entidade

        // Mapeia e retorna o DTO (já carregado dentro do @Transactional)
        return DetalheTrocaDTO.fromEntity(proposta);
    }


    // 2. Aceitar Proposta
    @Transactional // Garante que todas as operações dentro deste método sejam atômicas( atomicas é quando todas as operações são concluídas com sucesso ou nenhuma é aplicada)
    public DetalheTrocaDTO aceitarTroca(Long propostaId, Long usuarioReceptorId) {
        PropostaTroca proposta = propostaTrocaRepository.findById(propostaId)// Busca a proposta pelo ID
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada.")); // Lança exceção se não encontrada

        // Valida se o usuário receptor é o mesmo que está tentando aceitar a proposta
        if (!proposta.getUsuarioReceptor().getId().equals(usuarioReceptorId)) {
            throw new RuntimeException("Usuário não autorizado a aceitar esta proposta.");
        }
        if (!"PENDENTE".equals(proposta.getStatus())) {
            throw new RuntimeException("Proposta não está PENDENTE.");
        }

        Figurinha oferecida = proposta.getFigurinhaOferecida(); // Figurinha oferecida na proposta
        Figurinha desejada = proposta.getFigurinhaDesejada(); // Figurinha desejada na proposta

        // 1. Realiza a troca dos ÁLBUNS
        Album albumOfertante = oferecida.getAlbum();


        oferecida.setAlbum(desejada.getAlbum()); // Ofertada vai para o álbum do Receptor
        desejada.setAlbum(albumOfertante);       // Desejada vai para o álbum do Ofertante

        proposta.setStatus("ACEITA"); // Atualiza o status da proposta para ACEITA
        proposta = propostaTrocaRepository.save(proposta); // Salva o novo estado

        // Mapeia e retorna o DTO (já carregado dentro do @Transactional)
        return DetalheTrocaDTO.fromEntity(proposta);
    }

    @Transactional // Garante que todas as operações dentro deste método sejam atômicas ( atomicas é quando todas as operações são concluídas com sucesso ou nenhuma é aplicada)
    public void deletarTroca(Long propostaTrocaId) {// Método para deletar uma proposta de troca pelo ID
        // O método deleteById busca o item e o deleta. Se não for encontrado, a JPA lança uma exceção.
        // É recomendável verificar a existência antes ou tratar a exceção.
        if (!propostaTrocaRepository.existsById(propostaTrocaId)) {
            throw new RuntimeException("Proposta de troca não encontrada com o ID: " + propostaTrocaId);
        }
        propostaTrocaRepository.deleteById(propostaTrocaId);
    }


    @Transactional // Garante que todas as operações dentro deste método sejam atômicas ( atomicas é quando todas as operações são concluídas com sucesso ou nenhuma é aplicada)
    public DetalheTrocaDTO buscarDetalheTrocaPorId(Long propostaTrocaId) { // Método para buscar detalhes de uma proposta de troca pelo ID
        PropostaTroca proposta = propostaTrocaRepository.findById(propostaTrocaId) // Busca a proposta pelo ID
                .orElseThrow(() -> new RuntimeException("Proposta de troca não encontrada com o ID: " + propostaTrocaId)); // Lança exceção se não encontrada

        // Acesso forçado para carregar os dados LAZY antes de sair do método @Transactional
        proposta.getUsuarioOfertante().getNome(); // Força carregamento do ofertante
        proposta.getFigurinhaOferecida().getNomePersonagem(); // Força carregamento da figurinha oferecida
        // Os dados estão carregados, agora podemos mapear com segurança
        // Mapeia e retorna o DTO
        return DetalheTrocaDTO.fromEntity(proposta);
    }

}
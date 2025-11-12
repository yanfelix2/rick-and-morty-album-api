package com.albumrickandmortyapi.service;

import com.albumrickandmortyapi.client.RickAndMortyClient;
import com.albumrickandmortyapi.dto.AlbumDetalhadoDTO;
import com.albumrickandmortyapi.dto.CharacterDTO;
import com.albumrickandmortyapi.dto.FigurinhaDTO;
import com.albumrickandmortyapi.model.Album;
import com.albumrickandmortyapi.model.Figurinha;
import com.albumrickandmortyapi.model.Raridade;
import com.albumrickandmortyapi.repository.AlbumRepository;
import com.albumrickandmortyapi.repository.FigurinhaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

// Essa classe contém a lógica de negócio relacionada ao álbum e pacotes de figurinhas
@Service // Anotação para marcar esta classe como um serviço do Spring
public class AlbumService {

    // --- Injeção de Dependências ---
    private final AlbumRepository albumRepository;
    private final RickAndMortyClient rickAndMortyClient;
    private final FigurinhaRepository figurinhaRepository;
    private int totalPersonagens = 0;

    // Construtor para injeção de dependências
    public AlbumService(AlbumRepository albumRepository, RickAndMortyClient rickAndMortyClient, FigurinhaRepository figurinhaRepository) {
        this.figurinhaRepository = figurinhaRepository;
        this.albumRepository = albumRepository;
        this.rickAndMortyClient = rickAndMortyClient;
    }


    // --- Lógica de Abertura de Pacote ---
    @PostConstruct // O objetivo desse metodo é inicializar esse metodo assim que o serviço for criado( o serviço é criado quando a aplicação sobe)
    public void inicializarTotalPersonagens() {
        try {
            // Usa o Feign Client para obter o número total de personagens
            this.totalPersonagens = rickAndMortyClient.buscarDadosGerais().getInfo().getCount();
        } catch (Exception e) {
            // caso de erro, ele atribui um valor padrão e retorna uma mensagem de erro no console
            System.err.println("Erro ao inicializar o total de personagens da API: " + e.getMessage());
            this.totalPersonagens = 826; // Valor fallback
        }
    }



    // Esse meotodo serve para abrir um pacote de figurinhas para um usuário específico
    @Transactional // Garante que todas as operações dentro deste método sejam atômicas ( atomicas é quando todas as operações são concluídas com sucesso ou nenhuma é aplicada)
    public List<FigurinhaDTO> abrirPacote(Long usuarioId) {
        // Verifica se o total de personagens foi inicializado
        if (totalPersonagens == 0) throw new RuntimeException("O sistema ainda não carregou o total de personagens. Tente novamente em alguns segundos.");

        // Busca o álbum do usuário
        Album album = albumRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado para o ID: " + usuarioId));

        // Lista para guardar as entidades (para adicionar ao álbum)
        List<Figurinha> figurinhasPersistidas = new ArrayList<>();

        // Lista para guardar os DTOs (para o retorno da API)
        List<FigurinhaDTO> figurinhasRetornoDTO = new ArrayList<>(); // NOVA LISTA

        // Sorteia 5 figurinhas
        Random random = new Random();
        // Pega o total de personagens da API
        int totalPersonagens = this.totalPersonagens;

        // Loop para sortear 5 figurinhas
        for (int i = 0; i < 5; i++) {
            Long idSorteado = 1L + (long) random.nextInt(totalPersonagens);

            // Busca o personagem na API
            CharacterDTO personagemApi = rickAndMortyClient.buscarPersonagemPorId(idSorteado);

            // Cria a entidade Figurinha
            Figurinha nova = new Figurinha();
            nova.setIdPersonagemApi(personagemApi.getId()); // ID da API
            nova.setNomePersonagem(personagemApi.getName());   // Nome do personagem
            nova.setRaridade(determinarRaridade(personagemApi)); // Determina a raridade
            nova.setAlbum(album); // Associa ao álbum

            // 1. SALVA a entidade Figurinha
            Figurinha figurinhaSalva = figurinhaRepository.save(nova);

            figurinhasPersistidas.add(figurinhaSalva); // Adiciona à lista de figurinhas persistidas

            // 2. MAPEA para o DTO e adiciona à lista de retorno da API
            figurinhasRetornoDTO.add(FigurinhaDTO.fromEntity(figurinhaSalva));
        }

        // Adiciona as figurinhas PERSISTIDAS à lista do álbum em memória
        album.getFigurinhas().addAll(figurinhasPersistidas);

        // Salva o álbum (segurança)
        albumRepository.save(album);

        return figurinhasRetornoDTO; // RETORNA O DTO
    }

    // --- Lógica de Busca de Álbum ---
    public Album buscarAlbumPorUsuarioId(Long usuarioId) {
        return albumRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado para o ID: " + usuarioId));
    }

    // --- Lógica de Raridade ---
    private Raridade determinarRaridade(CharacterDTO personagem) {
        Random random = new Random();

        // 1. Usa equalsIgnoreCase para evitar problemas com maiúsculas/minúsculas
        if ("Alive".equalsIgnoreCase(personagem.getStatus())) {
            // 2. Os retornos são agora constantes do enum (Raridade.COMUM, Raridade.RARA)
            // 70% de chance de ser COMUM, 30% RARA
            return random.nextInt(100) < 70 ? Raridade.COMUM : Raridade.RARA;
        }

        // 2. Verifica se o status é "Dead", se for retorna RARA ou LENDARIA
        if ("Dead".equalsIgnoreCase(personagem.getStatus())) {
            // 60% RARA, 40% LENDÁRIA
            return random.nextInt(100) < 60 ? Raridade.RARA : Raridade.LENDARIA;
        }

        // Para "unknown" ou outros status, ou seja neutro, retorna ESPECIAL
        return Raridade.ESPECIAL;
    }


    // --- Lógica de Cálculo de Progresso ---
    public double calcularPorcentagemCompleta(Long usuarioId) {
        // Verifica se o total de personagens é maior que zero para evitar divisão por zero
        if (this.totalPersonagens == 0) return 0.0;

        // Busca o álbum do usuário
        Album album = buscarAlbumPorUsuarioId(usuarioId);

        // Usa Stream para contar apenas os IDs únicos dos personagens
        long figurinhasUnicas = album.getFigurinhas().stream()
                .map(Figurinha::getIdPersonagemApi)
                .distinct() // Garante que se ele tiver 5 Rick's, conte apenas 1
                .count();

        // Cálculo: (Únicas Possuídas / Total Geral) * 100
        double porcentagem = ((double) figurinhasUnicas / this.totalPersonagens) * 100.0;

        // Retorna a porcentagem com no máximo 2 casas decimais
        return Math.round(porcentagem * 100.0) / 100.0;
    }



    // --- Lógica de Detalhes do Álbum ---
    public AlbumDetalhadoDTO buscarDetalhesAlbumSimples(Long usuarioId) {

        // 1. Busca o Álbum e todas as figurinhas brutas
        Album album = albumRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado para o ID: " + usuarioId));

        //  Obtém a lista de figurinhas brutas
        List<Figurinha> figurinhasBrutas = album.getFigurinhas();

        // 2. Calcula o progresso
        double progresso = calcularPorcentagemCompleta(usuarioId);

        // 3. Lógica para CONTAGEM DE REPETIDAS usando Collectors.groupingBy
        Map<String, List<Figurinha>> agrupadas = figurinhasBrutas.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getNomePersonagem() + " (" + f.getRaridade() + ")"
                ));

        // 4. Cria um mapa que contém apenas a contagem das repetidas (quantidade > 1)
        Map<String, Integer> contagemRepetidas = agrupadas.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1) // Filtra apenas as duplicatas
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size() - 1 // Mapeia o nome para o número de CÓPIAS REPETIDAS
                ));

        // 5. Constrói o DTO de Resposta
        return AlbumDetalhadoDTO.builder()
                .figurinhasBrutas(figurinhasBrutas) // A lista original (para mostrar a coleção)
                .contagemDeRepetidas(contagemRepetidas)
                .progressoPercentual(progresso)
                .build();
    }


}
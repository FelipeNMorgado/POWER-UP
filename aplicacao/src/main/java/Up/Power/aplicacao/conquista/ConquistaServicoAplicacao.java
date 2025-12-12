package Up.Power.aplicacao.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.conquista.ConquistaService;
import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class ConquistaServicoAplicacao {

    // -------------------------------------------------------------------------
    // ADIÇÃO 1: Subject State Management (Lista de Observadores)
    // Usamos CopyOnWriteArrayList para thread safety, comum em ambientes Spring.
    private final List<ConquistaObserver> observers;
    // -------------------------------------------------------------------------

    private final ConquistaAssembler assembler;
    private final ConquistaService dominio;
    private final ConquistaRepository repository;
    private final PerfilRepository perfilRepository;

    public ConquistaServicoAplicacao(
            ConquistaAssembler assembler,
            ConquistaService dominio,
            ConquistaRepository repository,
            PerfilRepository perfilRepository,
            // -------------------------------------------------------------------
            // ADIÇÃO 2: Injeção de dependência de todos os ConquistaObserver
            // O Spring Boot automaticamente coleta todas as classes que implementam
            // a interface ConquistaObserver e as injeta aqui como uma lista.
            List<ConquistaObserver> observers
            // -------------------------------------------------------------------
    ) {
        this.assembler = assembler;
        this.dominio = dominio;
        this.repository = repository;
        this.perfilRepository = perfilRepository;

        // Inicializa a lista de observadores com os beans injetados.
        this.observers = new CopyOnWriteArrayList<>(observers);
    }

    // -------------------------------------------------------------------------
    // ADIÇÃO 3: Método de Notificação (O Coração do Padrão Observer)
    /**
     * Notifica todos os observadores inscritos sobre um evento de aplicação.
     * @param eventType Uma String ou Enum que identifica o tipo de evento (ex: "EXERCICIO_CONCLUIDO").
     * @param eventData Dados relevantes ao evento (ex: IDs, contadores, etc.).
     */
    public void notifyObservers(String eventType, Object eventData) {
        observers.forEach(o -> o.update(eventType, eventData));
    }
    // -------------------------------------------------------------------------

    // Métodos originais (sem modificação)
    public ConquistaResumo criar(
            Integer exercicioId,
            Integer treinoId,
            String nome,
            String descricao
    ) {
        Conquista conquista =
                assembler.criarDominio(exercicioId, treinoId, nome, descricao);

        dominio.adicionarConquistaAtiva(conquista);

        return assembler.toResumo(conquista, false, null);
    }

    public ConquistaResumo escolherBadge(Integer conquistaId, String badge) {

        Conquista conquista = repository.buscarPorId(new ConquistaId(conquistaId))
                .orElseThrow(() -> new RuntimeException("Conquista não encontrada"));

        boolean ok = dominio.escolherBadge(conquista, badge);

        return assembler.toResumo(
                conquista,
                ok,
                dominio.getBadgeAtual()
        );
    }

    public List<Conquista> listarConcluidas() {
        return dominio.getConquistasConcluidas();
    }

    public List<ConquistaResumo> listarTodas() {
        List<Conquista> conquistas = repository.listarAtivas();
        return conquistas.stream()
                .map(c -> assembler.toResumo(c, false, null))
                .toList();
    }

    public List<ConquistaResumo> listarPorPerfil(Integer perfilId) {
        Optional<Perfil> perfilOpt = perfilRepository.findById(new PerfilId(perfilId));
        if (perfilOpt.isEmpty()) {
            return List.of();
        }

        Perfil perfil = perfilOpt.get();
        return perfil.getConquistas().stream()
                .map(c -> assembler.toResumo(c, true, null))
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as conquistas do sistema com o status de cada uma para o perfil especificado.
     * @param perfilId ID do perfil do usuário
     * @return Lista de todas as conquistas com status de conclusão
     */
    public List<ConquistaResumo> listarTodasComStatus(Integer perfilId) {
        // Buscar todas as conquistas do sistema
        List<Conquista> todasConquistas = repository.listarAtivas();
        
        // Buscar conquistas do perfil
        List<ConquistaId> conquistasDoPerfil = List.of();
        Optional<Perfil> perfilOpt = perfilRepository.findById(new PerfilId(perfilId));
        if (perfilOpt.isPresent()) {
            Perfil perfil = perfilOpt.get();
            conquistasDoPerfil = perfil.getConquistas().stream()
                    .map(Conquista::getId)
                    .collect(Collectors.toList());
        }
        
        // Criar lista final com status de cada conquista
        final List<ConquistaId> finalConquistasDoPerfil = conquistasDoPerfil;
        List<ConquistaResumo> conquistasComStatus = todasConquistas.stream()
                .map(c -> {
                    boolean concluida = finalConquistasDoPerfil.contains(c.getId());
                    return assembler.toResumo(c, concluida, null);
                })
                .collect(Collectors.toList());
        
        // Ordenar: conquistas conquistadas primeiro (true vem antes de false)
        conquistasComStatus.sort((c1, c2) -> Boolean.compare(c2.concluida(), c1.concluida()));
        
        return conquistasComStatus;
    }
}
package Up.Power.aplicacao.planoTreino;

import Up.Power.Email;
import Up.Power.EstadoPlano;
import Up.Power.PlanoTreino;
import Up.Power.Treino;
// IMPORTAÇÕES CRÍTICAS PARA O PADRÃO OBSERVER
import Up.Power.aplicacao.conquista.ConquistaServicoAplicacao; // O Subject (Notificador)
import Up.Power.aplicacao.eventos.TreinoConcluidoEvent; // O DTO de Evento
// FIM DAS IMPORTAÇÕES CRÍTICAS
import Up.Power.aplicacao.conquista.ConquistaAvaliadorAplicacao;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.planoTreino.PlanoTreinoService;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para PlanoTreino.
 * Utiliza Strategy pattern para diferentes estratégias de validação de treinos.
 */
@Service
public class PlanoTreinoServicoAplicacao {

    private final PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao;
    private final PlanoTreinoService planoTreinoService;
    private final ValidacaoTreinoStrategy validacaoStrategy;

    // MUDANÇA 1: O campo injetado é o Subject/Notificador, não o Avaliador (Observer).
    private final ConquistaServicoAplicacao conquistaServicoAplicacao;

    private final PerfilRepository perfilRepository;

    public PlanoTreinoServicoAplicacao(
            PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao,
            PlanoTreinoRepository planoTreinoRepository,
            ValidacaoTreinoStrategy validacaoStrategy,
            // MUDANÇA 2: Injetamos o Subject (ConquistaServicoAplicacao)
            ConquistaServicoAplicacao conquistaServicoAplicacao,
            PerfilRepository perfilRepository) {

        this.planoTreinoRepositorioAplicacao = planoTreinoRepositorioAplicacao;
        this.planoTreinoService = new PlanoTreinoService(planoTreinoRepository);
        // Strategy pattern: estratégia injetada via Spring (padrão: ValidacaoTreinoCompletaStrategy)
        this.validacaoStrategy = validacaoStrategy;

        // MUDANÇA 3: Atribuição do Subject
        this.conquistaServicoAplicacao = conquistaServicoAplicacao;

        // Embora o Avaliador não seja mais chamado diretamente, se a injeção original
        // fosse assim, ela precisaria ser removida/ajustada.
        // O código original tinha: private final ConquistaAvaliadorAplicacao conquistaAvaliador;
        // Se essa injeção não for mais usada, ela deve ser removida.
        // Assumindo que o código foi limpo e apenas o ConquistaServicoAplicacao é necessário.

        this.perfilRepository = perfilRepository;
    }

    /**
     * Cria um novo plano de treino.
     */
    public PlanoTreinoResumo criarPlanoTreino(Integer id, String usuarioEmail, String nome) {
        // Se id for null, usar 0 (será gerado pelo banco)
        int planoTId = (id != null) ? id : 0;
        PlanoTreino plano = planoTreinoService.criarPlanoTreino(
                new PlanoTId(planoTId),
                new Email(usuarioEmail),
                nome
        );
        // Salvar o plano no repositório
        planoTreinoService.salvarPlanoTreino(plano);
        // Recarregar o plano salvo para obter o ID gerado pelo banco
        return planoTreinoRepositorioAplicacao.obterPorId(plano.getId())
                .map(PlanoTreinoResumoAssembler::toResumo)
                .orElse(PlanoTreinoResumoAssembler.toResumo(plano));
    }

    /**
     * Obtém um plano de treino por ID.
     */
    public PlanoTreinoResumo obterPorId(Integer id) {
        return planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(id))
                .map(PlanoTreinoResumoAssembler::toResumo)
                .orElse(null);
    }

    /**
     * Lista todos os planos de treino de um usuário.
     */
    public List<PlanoTreinoResumo> listarPorUsuario(String usuarioEmail) {
        return planoTreinoRepositorioAplicacao.listarPorUsuario(usuarioEmail).stream()
                .map(PlanoTreinoResumoAssembler::toResumo)
                .collect(Collectors.toList());
    }

    /**
     * Adiciona um treino ao plano usando a estratégia de validação configurada.
     */
    public PlanoTreinoResumo adicionarTreino(
            Integer planoTId,
            Integer treinoId,
            Integer exercicioId,
            TipoTreino tipo,
            Integer repeticoes,
            Float peso,
            Integer series,
            Float distancia,
            java.time.LocalDateTime tempo) {

        // Se treinoId for null, usar 0 (será gerado pelo banco)
        int treinoIdValue = (treinoId != null) ? treinoId : 0;

        // Cria o treino baseado no tipo
        Treino treino;
        if (tipo == TipoTreino.Cardio) {
            // Para Cardio, usar construtor simples e definir valores opcionais depois
            treino = new Treino(
                    new TreinoId(treinoIdValue),
                    new ExercicioId(exercicioId),
                    tipo
            );
            // Para Cardio, repeticoes, peso e series podem ser null/0
            if (repeticoes != null) {
                treino.setRepeticoes(repeticoes);
            }
            if (peso != null) {
                treino.setPeso(peso);
            }
            if (series != null) {
                treino.setSeries(series);
            }
        } else {
            // Para Peso, usar construtor completo com valores obrigatórios
            int repeticoesValue = (repeticoes != null) ? repeticoes : 0;
            float pesoValue = (peso != null) ? peso : 0f;
            int seriesValue = (series != null) ? series : 0;
            
            treino = new Treino(
                    new TreinoId(treinoIdValue),
                    new ExercicioId(exercicioId),
                    tipo,
                    repeticoesValue,
                    pesoValue,
                    seriesValue,
                    60 // descanso padrão
            );
        }

        // Define distancia e tempo se fornecidos
        if (distancia != null) {
            treino.setDistancia(distancia);
        }
        if (tempo != null) {
            treino.setTempo(tempo);
        }

        // Valida usando a estratégia configurada (Strategy pattern)
        validacaoStrategy.validar(treino);

        // Adiciona ao plano
        planoTreinoService.adicionarTreino(new PlanoTId(planoTId), treino);

        // Buscar o plano para obter o email do usuário
        Optional<PlanoTreino> planoOpt = planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(planoTId));
        if (planoOpt.isPresent()) {
            PlanoTreino plano = planoOpt.get();
            // Buscar perfil pelo email para obter o perfilId
            perfilRepository.findByUsuarioEmail(plano.getUsuarioEmail().getCaracteres())
                    .ifPresent(perfil -> {

                        // MUDANÇA 4: IMPLEMENTAÇÃO DO DISPARO DO OBSERVER
                        try {
                            // 1. Cria o DTO de Evento (TreinoConcluidoEvent)
                            TreinoConcluidoEvent evento = new TreinoConcluidoEvent(
                                    perfil.getId().getId(),
                                    treino
                            );

                            // 2. Dispara a notificação via Subject (ConquistaServicoAplicacao)
                            // O Subject notificará todos os Observers (como o ConquistaAvaliadorAplicacao)
                            conquistaServicoAplicacao.notifyObservers(
                                    "TREINO_CONCLUIDO", // O eventType que o Avaliador espera
                                    evento
                            );

                        } catch (Exception e) {
                            // Log do erro mas não interrompe a adição do treino
                            System.err.println("Erro ao notificar conquistas por treino (Observer): " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
        }

        return obterPorId(planoTId);
    }

    /**
     * Remove um treino do plano.
     */
    public PlanoTreinoResumo removerTreino(Integer planoTId, Integer treinoId) {
        if (planoTId == null) {
            throw new IllegalArgumentException("ID do plano não pode ser nulo");
        }
        if (treinoId == null) {
            throw new IllegalArgumentException("ID do treino não pode ser nulo");
        }

        PlanoTreino plano = planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(planoTId))
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado"));

        Treino treinoParaRemover = plano.getTreinos().stream()
                .filter(t -> t.getId() != null && t.getId().getId() == treinoId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado no plano. ID buscado: " + treinoId + ", Treinos no plano: " + plano.getTreinos().stream().map(t -> t.getId() != null ? String.valueOf(t.getId().getId()) : "null").toList()));

        planoTreinoService.removerTreino(new PlanoTId(planoTId), treinoParaRemover);
        return obterPorId(planoTId);
    }

    /**
     * Atualiza um treino no plano usando a estratégia de validação.
     */
    public PlanoTreinoResumo atualizarTreino(
            Integer planoTId,
            Integer treinoId,
            Integer exercicioId,
            TipoTreino tipo,
            Integer repeticoes,
            Float peso,
            Integer series,
            Float distancia,
            java.time.LocalDateTime tempo) {

        PlanoTreino plano = planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(planoTId))
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado"));

        Treino treinoAntigo = plano.getTreinos().stream()
                .filter(t -> t.getId().getId() == treinoId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado"));

        // Para atualizar, o treinoId não pode ser null
        if (treinoId == null) {
            throw new IllegalArgumentException("TreinoId não pode ser null ao atualizar um treino");
        }

        // Cria o treino baseado no tipo
        Treino treinoNovo;
        if (tipo == TipoTreino.Cardio) {
            // Para Cardio, usar construtor simples e definir valores opcionais depois
            treinoNovo = new Treino(
                    new TreinoId(treinoId),
                    new ExercicioId(exercicioId),
                    tipo
            );
            // Para Cardio, repeticoes, peso e series podem ser null/0
            if (repeticoes != null) {
                treinoNovo.setRepeticoes(repeticoes);
            }
            if (peso != null) {
                treinoNovo.setPeso(peso);
            }
            if (series != null) {
                treinoNovo.setSeries(series);
            }
        } else {
            // Para Peso, usar construtor completo com valores obrigatórios
            int repeticoesValue = (repeticoes != null) ? repeticoes : 0;
            float pesoValue = (peso != null) ? peso : 0f;
            int seriesValue = (series != null) ? series : 0;
            
            treinoNovo = new Treino(
                    new TreinoId(treinoId),
                    new ExercicioId(exercicioId),
                    tipo,
                    repeticoesValue,
                    pesoValue,
                    seriesValue,
                    60
            );
        }

        // Define distancia e tempo se fornecidos
        if (distancia != null) {
            treinoNovo.setDistancia(distancia);
        }
        if (tempo != null) {
            treinoNovo.setTempo(tempo);
        }

        // Valida usando a estratégia configurada
        validacaoStrategy.validar(treinoNovo);

        planoTreinoService.atualizarTreino(new PlanoTId(planoTId), treinoAntigo, treinoNovo);
        return obterPorId(planoTId);
    }

    /**
     * Adiciona um dia ao plano de treino.
     */
    public PlanoTreinoResumo adicionarDia(Integer planoTId, Dias dia) {
        planoTreinoService.adicionarDia(new PlanoTId(planoTId), dia);
        return obterPorId(planoTId);
    }

    /**
     * Atualiza todos os dias do plano de treino.
     */
    public PlanoTreinoResumo atualizarDias(Integer planoTId, List<Dias> dias) {
        planoTreinoService.atualizarDias(new PlanoTId(planoTId), dias);
        return obterPorId(planoTId);
    }

    /**
     * Altera o estado do plano de treino.
     */
    public PlanoTreinoResumo alterarEstado(Integer planoTId, EstadoPlano estado) {
        planoTreinoService.alterarEstadoPlano(new PlanoTId(planoTId), estado);
        return obterPorId(planoTId);
    }

    /**
     * Exclui um plano de treino.
     */
    public void excluirPlanoTreino(Integer planoTId) {
        planoTreinoService.excluirPlanoTreino(new PlanoTId(planoTId));
    }
}
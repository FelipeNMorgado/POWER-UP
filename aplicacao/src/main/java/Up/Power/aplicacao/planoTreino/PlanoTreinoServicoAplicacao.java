package Up.Power.aplicacao.planoTreino;

import Up.Power.Email;
import Up.Power.EstadoPlano;
import Up.Power.PlanoTreino;
import Up.Power.Treino;
import Up.Power.exercicio.ExercicioId;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.planoTreino.PlanoTreinoService;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public PlanoTreinoServicoAplicacao(
            PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao,
            PlanoTreinoRepository planoTreinoRepository,
            ValidacaoTreinoStrategy validacaoStrategy) {
        this.planoTreinoRepositorioAplicacao = planoTreinoRepositorioAplicacao;
        this.planoTreinoService = new PlanoTreinoService(planoTreinoRepository);
        // Strategy pattern: estratégia injetada via Spring (padrão: ValidacaoTreinoCompletaStrategy)
        this.validacaoStrategy = validacaoStrategy;
    }

    /**
     * Cria um novo plano de treino.
     */
    public PlanoTreinoResumo criarPlanoTreino(Integer id, String usuarioEmail, String nome) {
        PlanoTreino plano = planoTreinoService.criarPlanoTreino(
                new PlanoTId(id),
                new Email(usuarioEmail),
                nome
        );
        return PlanoTreinoResumoAssembler.toResumo(plano);
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
            Integer series) {
        
        // Cria o treino
        Treino treino = new Treino(
                new TreinoId(treinoId),
                new ExercicioId(exercicioId),
                tipo,
                repeticoes,
                peso,
                series,
                60 // descanso padrão
        );

        // Valida usando a estratégia configurada (Strategy pattern)
        validacaoStrategy.validar(treino);

        // Adiciona ao plano
        planoTreinoService.adicionarTreino(new PlanoTId(planoTId), treino);
        return obterPorId(planoTId);
    }

    /**
     * Remove um treino do plano.
     */
    public PlanoTreinoResumo removerTreino(Integer planoTId, Integer treinoId) {
        PlanoTreino plano = planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(planoTId))
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado"));
        
        Treino treinoParaRemover = plano.getTreinos().stream()
                .filter(t -> t.getId().getId() == treinoId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado no plano"));
        
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
            Integer series) {
        
        PlanoTreino plano = planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(planoTId))
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado"));
        
        Treino treinoAntigo = plano.getTreinos().stream()
                .filter(t -> t.getId().getId() == treinoId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado"));
        
        Treino treinoNovo = new Treino(
                new TreinoId(treinoId),
                new ExercicioId(exercicioId),
                tipo,
                repeticoes,
                peso,
                series,
                60
        );

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


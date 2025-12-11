package Up.Power.aplicacao.meta;

import Up.Power.Meta;
import Up.Power.PlanoTreino;
import Up.Power.Treino;
import Up.Power.exercicio.ExercicioId;
import Up.Power.meta.MetaId;
import Up.Power.meta.RewardService;
import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TreinoId;
import Up.Power.treinoProgresso.TreinoProgresso;
import Up.Power.treinoProgresso.TreinoProgressoRepository;
import Up.Power.aplicacao.planoTreino.PlanoTreinoRepositorioAplicacao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MetaServicoAplicacao {

    private final MetaRepositorioAplicacao metaRepositorioAplicacao;
    private final RewardService rewardService;
    private final TreinoProgressoRepository treinoProgressoRepository;
    private final PerfilRepository perfilRepository;
    private final PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao;

    public MetaServicoAplicacao(
            MetaRepositorioAplicacao metaRepositorioAplicacao,
            RewardService rewardService,
            TreinoProgressoRepository treinoProgressoRepository,
            PerfilRepository perfilRepository,
            PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao
    ) {
        this.metaRepositorioAplicacao = metaRepositorioAplicacao;
        this.rewardService = rewardService;
        this.treinoProgressoRepository = treinoProgressoRepository;
        this.perfilRepository = perfilRepository;
        this.planoTreinoRepositorioAplicacao = planoTreinoRepositorioAplicacao;
    }

    public MetaResumo obterPorId(Integer id) {
        return metaRepositorioAplicacao.obterPorId(new MetaId(id))
                .map(meta -> MetaResumoAssembler.toResumo(meta, false)) // Sem perfilId, não verifica conclusão
                .orElse(null);
    }

    public List<MetaResumo> obterMetasPorUsuario(int userId) {
        PerfilId perfilId = new PerfilId(userId);
        return metaRepositorioAplicacao.obterPorUsuario(userId).stream()
                .map(meta -> MetaResumoAssembler.toResumo(meta, verificarConclusao(meta, perfilId)))
                .toList();
    }

    public MetaResumo obterPorId(Integer id, Integer perfilId) {
        return metaRepositorioAplicacao.obterPorId(new MetaId(id))
                .map(meta -> MetaResumoAssembler.toResumo(meta, verificarConclusao(meta, new PerfilId(perfilId))))
                .orElse(null);
    }

    public boolean podeColetarRecompensas(Integer metaId) {
        Optional<Meta> metaOpt = metaRepositorioAplicacao.obterPorId(new MetaId(metaId));
        if (metaOpt.isEmpty()) {
            return false;
        }

        // Usa o serviço de domínio para verificar a regra de negócio
        return rewardService.canCollectRewards(metaOpt.get(), LocalDate.now());
    }

    public MetaResumo criar(CriarMetaRequest request) {
        ExercicioId exercicioId = request.exercicioId() != null ? new ExercicioId(request.exercicioId()) : null;
        TreinoId treinoId = request.treinoId() != null ? new TreinoId(request.treinoId()) : null;
        
        Meta novaMeta = new Meta(
                new MetaId(0), // ID será gerado pelo banco (0 será convertido para null no mapper)
                exercicioId,
                treinoId,
                request.nome(),
                request.dataFim(),
                request.dataInicio(),
                request.exigenciaMinima()
        );
        
        Meta metaSalva = metaRepositorioAplicacao.salvar(novaMeta);
        return MetaResumoAssembler.toResumo(metaSalva, false); // Nova meta não está concluída ainda
    }

    public MetaResumo atualizar(Integer id, CriarMetaRequest request) {
        Optional<Meta> metaOpt = metaRepositorioAplicacao.obterPorId(new MetaId(id));
        if (metaOpt.isEmpty()) {
            throw new IllegalArgumentException("Meta não encontrada com ID: " + id);
        }

        Meta meta = metaOpt.get();
        ExercicioId exercicioId = request.exercicioId() != null ? new ExercicioId(request.exercicioId()) : null;
        TreinoId treinoId = request.treinoId() != null ? new TreinoId(request.treinoId()) : null;
        
        // Criar nova instância com dados atualizados
        Meta metaAtualizada = new Meta(
                meta.getId(),
                exercicioId,
                treinoId,
                request.nome(),
                request.dataFim(),
                request.dataInicio(),
                request.exigenciaMinima()
        );
        
        Meta metaSalva = metaRepositorioAplicacao.salvar(metaAtualizada);
        // Verificar conclusão após atualizar (precisa do perfilId, mas não temos aqui - será verificado na listagem)
        return MetaResumoAssembler.toResumo(metaSalva, false);
    }

    public void deletar(Integer id) {
        metaRepositorioAplicacao.deletar(new MetaId(id));
    }

    /**
     * Verifica se uma meta foi concluída baseado no progresso do usuário.
     * A meta é considerada concluída se o usuário atingiu a exigência mínima.
     * Verifica tanto nos registros de progresso quanto nos treinos dos planos de treino.
     */
    private boolean verificarConclusao(Meta meta, PerfilId perfilId) {
        if (meta.getExigenciaMinima() == null) {
            return false; // Sem exigência, não pode ser concluída automaticamente
        }

        Double pesoMaximo = 0.0;

        // 1. Verificar registros de progresso (treino_progresso)
        if (meta.getExercicioId() != null) {
            List<TreinoProgresso> progressos = treinoProgressoRepository.listarPorPerfilEExercicio(
                    perfilId, 
                    meta.getExercicioId()
            );
            
            Double pesoMaximoProgresso = progressos.stream()
                    .filter(p -> p.getPesoKg() != null)
                    .map(TreinoProgresso::getPesoKg)
                    .max(Double::compareTo)
                    .orElse(0.0);
            
            pesoMaximo = Math.max(pesoMaximo, pesoMaximoProgresso);
        }

        // 2. Verificar treinos nos planos de treino do usuário
        try {
            Optional<Perfil> perfilOpt = perfilRepository.findById(perfilId);
            if (perfilOpt.isPresent()) {
                Perfil perfil = perfilOpt.get();
                String usuarioEmail = perfil.getUsuarioEmail().getCaracteres();
                List<PlanoTreino> planosTreino = planoTreinoRepositorioAplicacao.listarPorUsuario(usuarioEmail);
                
                // Se a meta tem treinoId específico, verificar esse treino
                if (meta.getTreinoId() != null) {
                    for (PlanoTreino plano : planosTreino) {
                        for (Treino treino : plano.getTreinos()) {
                            if (treino.getId() != null && treino.getId().equals(meta.getTreinoId())) {
                                // Verificar tanto o peso atual quanto o recorde de carga
                                pesoMaximo = Math.max(pesoMaximo, (double) treino.getPeso());
                                pesoMaximo = Math.max(pesoMaximo, (double) treino.getRecordeCarga());
                            }
                        }
                    }
                }
                
                // Se a meta tem exercicioId, verificar todos os treinos desse exercício
                if (meta.getExercicioId() != null) {
                    for (PlanoTreino plano : planosTreino) {
                        for (Treino treino : plano.getTreinos()) {
                            if (treino.getExercicio() != null && 
                                treino.getExercicio().equals(meta.getExercicioId())) {
                                // Verificar tanto o peso atual quanto o recorde de carga
                                pesoMaximo = Math.max(pesoMaximo, (double) treino.getPeso());
                                pesoMaximo = Math.max(pesoMaximo, (double) treino.getRecordeCarga());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Se houver erro ao buscar planos de treino, continuar apenas com progresso
            System.err.println("Erro ao verificar planos de treino para conclusão de meta: " + e.getMessage());
        }
        
        return pesoMaximo >= meta.getExigenciaMinima();
    }
}
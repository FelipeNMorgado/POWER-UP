package Up.Power.aplicacao.frequencia;

import Up.Power.Frequencia;
import Up.Power.Treino;
import Up.Power.aplicacao.avatar.AdicionarXpCommand;
import Up.Power.aplicacao.avatar.AvatarServicoAplicacao;
import Up.Power.aplicacao.planoTreino.PlanoTreinoRepositorioAplicacao;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.frequencia.FrequenciaRepository;
import Up.Power.frequencia.FrequenciaService;
import Up.Power.perfil.PerfilId;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FrequenciaServicoAplicacao {

    private final FrequenciaRepository repo;
    private final FrequenciaService frequenciaService;
    private final PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao;
    private final AvatarServicoAplicacao avatarServicoAplicacao;

    private static final int XP_POR_FREQUENCIA = 10; // XP ganho por cada ida à academia

    public FrequenciaServicoAplicacao(
            FrequenciaRepository repo, 
            FrequenciaService frequenciaService,
            PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao,
            AvatarServicoAplicacao avatarServicoAplicacao) {
        this.repo = repo;
        this.frequenciaService = frequenciaService;
        this.planoTreinoRepositorioAplicacao = planoTreinoRepositorioAplicacao;
        this.avatarServicoAplicacao = avatarServicoAplicacao;
    }

    public FrequenciaResumo obterPorId(Integer id) {
        var freq = repo.obterFrequencia(new FrequenciaId(id), null);
        return freq == null ? null : toResumo(freq);
    }

    public List<FrequenciaResumo> listarPorPerfil(Integer perfilId) {
        return repo.listarPorPerfil(perfilId)
                .stream()
                .map(this::toResumo)
                .collect(Collectors.toList());
    }

    public void registrarPresenca(Integer perfilId, Integer treinoId, Integer planoTreinoId) {
        frequenciaService.registrarPresenca(
            new PerfilId(perfilId),
            new TreinoId(treinoId),
            new PlanoTId(planoTreinoId)
        );
        
        // Adicionar XP ao avatar após registrar frequência
        adicionarXpAoAvatar(perfilId);
        
        // Se o treino for do tipo Peso, aumentar força em 1
        verificarEAumentarForca(perfilId, treinoId, planoTreinoId);
    }

    public void registrarPresencaComFoto(Integer perfilId, Integer treinoId, Integer planoTreinoId, String fotoBase64) {
        frequenciaService.registrarPresencaComFoto(
            new PerfilId(perfilId),
            new TreinoId(treinoId),
            new PlanoTId(planoTreinoId),
            fotoBase64
        );
        
        // Adicionar XP ao avatar após registrar frequência
        adicionarXpAoAvatar(perfilId);
        
        // Se o treino for do tipo Peso, aumentar força em 1
        verificarEAumentarForca(perfilId, treinoId, planoTreinoId);
    }

    /**
     * Registra presença automaticamente baseado no dia da semana atual.
     * Busca o plano de treino que tem o dia atual configurado.
     */
    public void registrarPresencaAutomatica(Integer perfilId, String usuarioEmail) {
        // Obter dia da semana atual
        LocalDate hoje = LocalDate.now();
        DayOfWeek diaSemana = hoje.getDayOfWeek();
        Dias diaAtual = converterDayOfWeekParaDias(diaSemana);

        // Buscar todos os planos ativos do usuário
        List<Up.Power.PlanoTreino> planosAtivos = planoTreinoRepositorioAplicacao.listarPorUsuario(usuarioEmail)
                .stream()
                .filter(p -> p.getEstadoPlano() == Up.Power.EstadoPlano.Ativo)
                .filter(p -> p.getDias().contains(diaAtual))
                .collect(Collectors.toList());

        if (planosAtivos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum plano de treino ativo encontrado para " + diaAtual + "-feira");
        }

        // Verificar se já existe frequência para hoje
        List<Frequencia> frequenciasHoje = repo.listarPorPerfilEData(perfilId, hoje);
        if (!frequenciasHoje.isEmpty()) {
            throw new IllegalStateException("Você já registrou presença hoje. Não é possível registrar novamente no mesmo dia.");
        }

        // Usar o primeiro plano encontrado (se houver múltiplos, usa o primeiro)
        Up.Power.PlanoTreino plano = planosAtivos.get(0);
        PlanoTId planoTId = plano.getId();
        
        // Usar o primeiro treino do plano ou um treino padrão
        Integer treinoId = plano.getTreinos().isEmpty() ? 1 : plano.getTreinos().get(0).getId().getId();

        frequenciaService.registrarPresenca(
            new PerfilId(perfilId),
            new TreinoId(treinoId),
            planoTId
        );
        
        // Adicionar XP ao avatar após registrar frequência
        adicionarXpAoAvatar(perfilId);
        
        // Se o treino for do tipo Peso, aumentar força em 1
        verificarEAumentarForca(perfilId, treinoId, planoTId.getId());
    }

    /**
     * Registra presença com foto automaticamente baseado no dia da semana atual.
     */
    public void registrarPresencaComFotoAutomatica(Integer perfilId, String usuarioEmail, String fotoBase64) {
        // Obter dia da semana atual
        LocalDate hoje = LocalDate.now();
        DayOfWeek diaSemana = hoje.getDayOfWeek();
        Dias diaAtual = converterDayOfWeekParaDias(diaSemana);

        // Buscar todos os planos ativos do usuário
        List<Up.Power.PlanoTreino> planosAtivos = planoTreinoRepositorioAplicacao.listarPorUsuario(usuarioEmail)
                .stream()
                .filter(p -> p.getEstadoPlano() == Up.Power.EstadoPlano.Ativo)
                .filter(p -> p.getDias().contains(diaAtual))
                .collect(Collectors.toList());

        if (planosAtivos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum plano de treino ativo encontrado para " + diaAtual + "-feira");
        }

        // Verificar se já existe frequência para hoje
        List<Frequencia> frequenciasHoje = repo.listarPorPerfilEData(perfilId, hoje);
        if (!frequenciasHoje.isEmpty()) {
            throw new IllegalStateException("Você já registrou presença hoje. Não é possível registrar novamente no mesmo dia.");
        }

        // Usar o primeiro plano encontrado
        Up.Power.PlanoTreino plano = planosAtivos.get(0);
        PlanoTId planoTId = plano.getId();
        
        // Usar o primeiro treino do plano ou um treino padrão
        Integer treinoId = plano.getTreinos().isEmpty() ? 1 : plano.getTreinos().get(0).getId().getId();

        frequenciaService.registrarPresencaComFoto(
            new PerfilId(perfilId),
            new TreinoId(treinoId),
            planoTId,
            fotoBase64
        );
        
        // Adicionar XP ao avatar após registrar frequência
        adicionarXpAoAvatar(perfilId);
        
        // Se o treino for do tipo Peso, aumentar força em 1
        verificarEAumentarForca(perfilId, treinoId, planoTId.getId());
    }

    /**
     * Verifica se o treino é do tipo Peso e, se for, aumenta a força do avatar em 1.
     */
    private void verificarEAumentarForca(Integer perfilId, Integer treinoId, Integer planoTreinoId) {
        try {
            // Obter o plano de treino
            var planoOpt = planoTreinoRepositorioAplicacao.obterPorId(new PlanoTId(planoTreinoId));
            if (planoOpt.isEmpty()) {
                System.err.println("Plano de treino não encontrado para verificar tipo: " + planoTreinoId);
                return;
            }

            Up.Power.PlanoTreino plano = planoOpt.get();
            
            // Buscar o treino na lista de treinos do plano
            Treino treino = plano.getTreinos().stream()
                    .filter(t -> t.getId() != null && t.getId().getId() == treinoId)
                    .findFirst()
                    .orElse(null);

            if (treino == null) {
                System.err.println("Treino não encontrado no plano: " + treinoId);
                return;
            }

            // Se o treino for do tipo Peso, aumentar força em 5
            if (treino.getTipo() == TipoTreino.Peso) {
                avatarServicoAplicacao.adicionarForca(perfilId, 5);
            }
        } catch (Exception e) {
            // Log do erro mas não interrompe o registro de frequência
            System.err.println("Erro ao verificar tipo de treino e aumentar força: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Dias converterDayOfWeekParaDias(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> Dias.Segunda;
            case TUESDAY -> Dias.Terca;
            case WEDNESDAY -> Dias.Quarta;
            case THURSDAY -> Dias.Quinta;
            case FRIDAY -> Dias.Sexta;
            case SATURDAY -> Dias.Sabado;
            case SUNDAY -> Dias.Domingo;
        };
    }

    public int calcularSequenciaDias(Integer perfilId, Integer planoTreinoId) {
        return frequenciaService.calcularSequenciaDias(
            new PerfilId(perfilId),
            new PlanoTId(planoTreinoId)
        );
    }

    public int calcularFrequenciaSemanal(Integer perfilId, Integer planoTreinoId) {
        return frequenciaService.calcularFrequenciaSemanal(
            new PerfilId(perfilId),
            new PlanoTId(planoTreinoId)
        );
    }

    /**
     * Calcula a sequência de dias consecutivos considerando todas as frequências do perfil
     */
    public int calcularSequenciaDiasTotal(Integer perfilId) {
        return frequenciaService.calcularSequenciaDiasTotal(new PerfilId(perfilId));
    }

    /**
     * Calcula a sequência de dias consecutivos desde uma data específica
     */
    public int calcularSequenciaDiasDesdeData(Integer perfilId, LocalDate dataReferencia) {
        return frequenciaService.calcularSequenciaDiasDesdeData(new PerfilId(perfilId), dataReferencia);
    }

    /**
     * Adiciona XP ao avatar quando uma frequência é registrada.
     * A avaliação de conquistas por atributos será feita automaticamente
     * pelo AvatarServicoAplicacao quando o XP for adicionado.
     */
    private void adicionarXpAoAvatar(Integer perfilId) {
        try {
            avatarServicoAplicacao.adicionarXp(new AdicionarXpCommand(perfilId, XP_POR_FREQUENCIA));
        } catch (Exception e) {
            // Log do erro mas não interrompe o registro de frequência
            System.err.println("Erro ao adicionar XP ao avatar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private FrequenciaResumo toResumo(Frequencia f) {
        return new FrequenciaResumo(
                f.getId().getId(),
                f.getPerfil().getId(),
                f.getTreino().getId(),
                f.getDataDePresenca(),
                f.getPlanoTId() != null ? f.getPlanoTId().getId() : null,
                f.getFoto()
        );
    }
}

package Up.Power.aplicacao.equipe;

import Up.Power.Equipe;
import Up.Power.Email;
import Up.Power.equipe.EquipeId;
import Up.Power.equipe.EquipeRepository;
import Up.Power.equipe.EquipeService;
import Up.Power.perfil.PerfilRepository;
import Up.Power.aplicacao.frequencia.FrequenciaServicoAplicacao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Serviço de aplicação para Equipe.
 * Orquestra operações de equipe.
 */
@Service
public class EquipeServicoAplicacao {

    private final EquipeRepositorioAplicacao equipeRepositorioAplicacao;
    private final EquipeService equipeService;
    private final PerfilRepository perfilRepository;
    private final FrequenciaServicoAplicacao frequenciaServicoAplicacao;

    public EquipeServicoAplicacao(
            EquipeRepositorioAplicacao equipeRepositorioAplicacao,
            EquipeRepository equipeRepository,
            PerfilRepository perfilRepository,
            FrequenciaServicoAplicacao frequenciaServicoAplicacao) {
        this.equipeRepositorioAplicacao = equipeRepositorioAplicacao;
        this.equipeService = new EquipeService(equipeRepository);
        this.perfilRepository = perfilRepository;
        this.frequenciaServicoAplicacao = frequenciaServicoAplicacao;
    }

    /**
     * Cria uma nova equipe.
     */
    public EquipeResumo criarEquipe(Integer id, String nome, String usuarioAdmEmail, String descricao) {
        // Se id for null ou 0, usar 0 para que o banco gere automaticamente
        int equipeId = (id == null || id == 0) ? 0 : id;
        Equipe equipe = equipeService.criarEquipe(
                new EquipeId(equipeId),
                nome,
                new Email(usuarioAdmEmail)
        );
        
        // Definir descrição se fornecida
        if (descricao != null && !descricao.trim().isEmpty()) {
            equipe.setDescricao(descricao.trim());
        }
        
        equipeRepositorioAplicacao.salvar(equipe);
        return EquipeResumoAssembler.toResumo(equipe);
    }

    /**
     * Obtém uma equipe por ID (excluindo equipes expiradas).
     */
    public EquipeResumo obterPorId(Integer id) {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        return equipeRepositorioAplicacao.obterPorId(new EquipeId(id))
                .filter(equipe -> equipe.getFim() == null || !equipe.getFim().isBefore(hoje))
                .map(EquipeResumoAssembler::toResumo)
                .orElse(null);
    }

    /**
     * Lista todas as equipes de um usuário (excluindo equipes expiradas).
     */
    public List<EquipeResumo> listarPorUsuario(String usuarioEmail) {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        return equipeRepositorioAplicacao.listarPorUsuario(usuarioEmail).stream()
                .filter(equipe -> equipe.getFim() == null || !equipe.getFim().isBefore(hoje))
                .map(EquipeResumoAssembler::toResumo)
                .collect(Collectors.toList());
    }

    /**
     * Adiciona um membro à equipe.
     */
    public EquipeResumo adicionarMembro(Integer equipeId, String novoMembroEmail) {
        EquipeId id = new EquipeId(equipeId);
        equipeService.adicionarMembro(id, new Email(novoMembroEmail));
        return obterPorId(equipeId);
    }

    /**
     * Remove um membro da equipe (apenas líderes podem fazer isso).
     */
    public EquipeResumo removerMembro(Integer equipeId, String membroEmail, String usuarioEmail) {
        EquipeId id = new EquipeId(equipeId);
        
        // Verificar se o usuário é líder
        if (!isLider(equipeId, usuarioEmail)) {
            throw new IllegalStateException("Apenas o líder da equipe pode expulsar membros.");
        }
        
        // Verificar se não está tentando expulsar o próprio líder
        Equipe equipe = equipeRepositorioAplicacao.obterPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        
        if (equipe.getUsuarioAdm().getCaracteres().equals(membroEmail)) {
            throw new IllegalStateException("Não é possível expulsar o líder da equipe.");
        }
        
        equipeService.removerMembro(id, new Email(membroEmail));
        return obterPorId(equipeId);
    }

    /**
     * Atualiza informações da equipe.
     */
    public EquipeResumo atualizarInformacoes(Integer equipeId, String nome, String descricao, String foto) {
        EquipeId id = new EquipeId(equipeId);
        // Verificar se a equipe existe e não está expirada
        Equipe equipe = equipeRepositorioAplicacao.obterPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        
        // Verificar se a data de fim já passou
        if (equipe.getFim() != null && equipe.getFim().isBefore(java.time.LocalDate.now())) {
            throw new IllegalStateException("Não é possível editar uma equipe que já expirou.");
        }
        
        equipeService.atualizarInformacoes(id, nome, descricao, foto);
        return obterPorId(equipeId);
    }

    /**
     * Define período de funcionamento da equipe.
     */
    public EquipeResumo definirPeriodo(Integer equipeId, LocalDate inicio, LocalDate fim) {
        EquipeId id = new EquipeId(equipeId);
        // Verificar se a equipe existe e não está expirada
        Equipe equipe = equipeRepositorioAplicacao.obterPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        
        // Verificar se a data de fim já passou
        if (equipe.getFim() != null && equipe.getFim().isBefore(java.time.LocalDate.now())) {
            throw new IllegalStateException("Não é possível editar uma equipe que já expirou.");
        }
        
        equipeService.definirPeriodo(id, inicio, fim);
        return obterPorId(equipeId);
    }

    /**
     * Verifica se um usuário é líder da equipe.
     */
    public boolean isLider(Integer equipeId, String usuarioEmail) {
        return equipeService.isLider(new EquipeId(equipeId), new Email(usuarioEmail));
    }

    /**
     * Verifica se um usuário é membro da equipe.
     */
    public boolean isMembro(Integer equipeId, String usuarioEmail) {
        return equipeService.isMembro(new EquipeId(equipeId), new Email(usuarioEmail));
    }

    /**
     * Lista todos os emails dos membros da equipe.
     */
    public List<String> listarMembros(Integer equipeId) {
        Equipe equipe = equipeRepositorioAplicacao.obterPorId(new EquipeId(equipeId))
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        
        return equipe.getUsuariosEmails().stream()
                .map(Email::getCaracteres)
                .collect(Collectors.toList());
    }

    /**
     * Exclui uma equipe (apenas líderes podem fazer isso).
     */
    public void excluirEquipe(Integer equipeId, String usuarioEmail) {
        EquipeId id = new EquipeId(equipeId);
        
        // Verificar se o usuário é líder
        if (!isLider(equipeId, usuarioEmail)) {
            throw new IllegalStateException("Apenas o líder da equipe pode excluí-la.");
        }
        
        // Verificar se a equipe existe
        equipeRepositorioAplicacao.obterPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        
        // Excluir a equipe
        equipeRepositorioAplicacao.excluir(id);
    }

    /**
     * Obtém o ranking da equipe baseado em dias consecutivos desde a entrada na equipe.
     */
    public List<MembroRanking> obterRanking(Integer equipeId) {
        Equipe equipe = equipeRepositorioAplicacao.obterPorId(new EquipeId(equipeId))
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        
        // Usar a data de início da equipe como referência, ou data atual se não houver
        LocalDate dataReferencia = equipe.getInicio() != null ? equipe.getInicio() : LocalDate.now();
        
        List<MembroRanking> ranking = new ArrayList<>();
        
        for (Email emailMembro : equipe.getUsuariosEmails()) {
            String emailStr = emailMembro.getCaracteres();
            
            // Buscar perfil pelo email
            var perfilOpt = perfilRepository.findByUsuarioEmail(emailStr);
            if (perfilOpt.isEmpty()) {
                // Se não encontrar perfil, adiciona com 0 dias
                ranking.add(new MembroRanking(emailStr, 0));
                continue;
            }
            
            var perfil = perfilOpt.get();
            Integer perfilId = perfil.getId().getId();
            
            // Calcular sequência de dias desde a data de referência
            int diasConsecutivos = frequenciaServicoAplicacao.calcularSequenciaDiasDesdeData(perfilId, dataReferencia);
            
            ranking.add(new MembroRanking(emailStr, diasConsecutivos));
        }
        
        // Ordenar por dias consecutivos (maior primeiro), depois por email (alfabético)
        ranking.sort(Comparator
                .comparingInt((MembroRanking m) -> m.diasConsecutivos()).reversed()
                .thenComparing(MembroRanking::email));
        
        return ranking;
    }

    /**
     * DTO para representar um membro no ranking
     */
    public record MembroRanking(String email, int diasConsecutivos) {}
}


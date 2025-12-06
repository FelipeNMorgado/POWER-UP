package Up.Power.aplicacao.equipe;

import Up.Power.Equipe;
import Up.Power.Email;
import Up.Power.equipe.EquipeId;
import Up.Power.equipe.EquipeRepository;
import Up.Power.equipe.EquipeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public EquipeServicoAplicacao(
            EquipeRepositorioAplicacao equipeRepositorioAplicacao,
            EquipeRepository equipeRepository) {
        this.equipeRepositorioAplicacao = equipeRepositorioAplicacao;
        this.equipeService = new EquipeService(equipeRepository);
    }

    /**
     * Cria uma nova equipe.
     */
    public EquipeResumo criarEquipe(Integer id, String nome, String usuarioAdmEmail) {
        Equipe equipe = equipeService.criarEquipe(
                new EquipeId(id),
                nome,
                new Email(usuarioAdmEmail)
        );
        return EquipeResumoAssembler.toResumo(equipe);
    }

    /**
     * Obtém uma equipe por ID.
     */
    public EquipeResumo obterPorId(Integer id) {
        return equipeRepositorioAplicacao.obterPorId(new EquipeId(id))
                .map(EquipeResumoAssembler::toResumo)
                .orElse(null);
    }

    /**
     * Lista todas as equipes de um usuário.
     */
    public List<EquipeResumo> listarPorUsuario(String usuarioEmail) {
        return equipeRepositorioAplicacao.listarPorUsuario(usuarioEmail).stream()
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
     * Remove um membro da equipe.
     */
    public EquipeResumo removerMembro(Integer equipeId, String membroEmail) {
        EquipeId id = new EquipeId(equipeId);
        equipeService.removerMembro(id, new Email(membroEmail));
        return obterPorId(equipeId);
    }

    /**
     * Atualiza informações da equipe.
     */
    public EquipeResumo atualizarInformacoes(Integer equipeId, String nome, String descricao, String foto) {
        EquipeId id = new EquipeId(equipeId);
        equipeService.atualizarInformacoes(id, nome, descricao, foto);
        return obterPorId(equipeId);
    }

    /**
     * Define período de funcionamento da equipe.
     */
    public EquipeResumo definirPeriodo(Integer equipeId, LocalDate inicio, LocalDate fim) {
        EquipeId id = new EquipeId(equipeId);
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
}


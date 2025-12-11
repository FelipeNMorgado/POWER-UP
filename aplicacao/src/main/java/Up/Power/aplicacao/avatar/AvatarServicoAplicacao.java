package Up.Power.aplicacao.avatar;

import Up.Power.Avatar;
import Up.Power.aplicacao.conquista.ConquistaAvaliadorAplicacao;
import Up.Power.avatar.AvatarService;
import Up.Power.avatar.ExperienceService;
import Up.Power.avatar.AvatarId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AvatarServicoAplicacao {

    private final AvatarRepositorioAplicacao avatarRepositorio;
    private final AvatarService avatarDominioService;
    private final ExperienceService experienceService;
    private final ConquistaAvaliadorAplicacao conquistaAvaliador;

    public AvatarServicoAplicacao(
            AvatarRepositorioAplicacao avatarRepositorio,
            AvatarService avatarDominioService,
            ExperienceService experienceService,
            @Lazy ConquistaAvaliadorAplicacao conquistaAvaliador
    ) {
        this.avatarRepositorio = avatarRepositorio;
        this.avatarDominioService = avatarDominioService;
        this.experienceService = experienceService;
        this.conquistaAvaliador = conquistaAvaliador;
    }

    public AvatarResumo obterPorPerfilId(Integer perfilId) {
        return avatarRepositorio.obterPorPerfilId(perfilId)
                .map(avatar -> {
                    System.out.println("Avatar carregado - ID: " + avatar.getId().getId() + 
                                     ", Dinheiro: " + avatar.getDinheiro() + 
                                     ", PerfilId: " + avatar.getPerfil().getId());
                    return AvatarResumoAssembler.toResumo(avatar);
                })
                .orElse(null);
    }

    public void adicionarXp(AdicionarXpCommand command) {
        Optional<Avatar> avatarOpt = avatarRepositorio.obterPorPerfilId(command.perfilId());

        if (avatarOpt.isEmpty()) {
            throw new NoSuchElementException("Avatar para o Perfil " + command.perfilId() + " não encontrado.");
        }

        Avatar avatar = avatarOpt.get();
        
        // 1. Chama o serviço de domínio para aplicar a lógica de XP/Nível
        experienceService.adicionarXp(avatar, command.xpGanha());
        
        // 2. Persiste a entidade de domínio atualizada (salva novo Nível/XP)
        avatarRepositorio.salvar(avatar);
        
        // 3. Avaliar conquistas baseadas nos atributos atualizados do avatar
        try {
            conquistaAvaliador.avaliarEAdicionarConquistasPorAtributos(
                    command.perfilId(),
                    avatar
            );
        } catch (Exception e) {
            // Log do erro mas não interrompe a adição de XP
            System.err.println("Erro ao avaliar conquistas por atributos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // DTO para retornar atributos calculados na camada de aplicação
    public record AtributosCalculadosResumo(int forca, int resistencia, int agilidade) {}

    // Método para expor atributos calculados usando o AvatarService de domínio
    public AtributosCalculadosResumo obterAtributosCalculados(Integer avatarId) {
        AvatarId id = new AvatarId(avatarId); 
        
        // O AvatarService do domínio calcula os atributos com base no Nível
        int forca = avatarDominioService.getForca(id);
        int resistencia = avatarDominioService.getResistencia(id);
        int agilidade = avatarDominioService.getAgilidade(id);
        
        return new AtributosCalculadosResumo(forca, resistencia, agilidade);
    }
}
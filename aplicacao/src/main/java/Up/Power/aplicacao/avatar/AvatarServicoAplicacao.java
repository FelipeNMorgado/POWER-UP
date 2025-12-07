package Up.Power.aplicacao.avatar;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarService;
import Up.Power.avatar.ExperienceService;
import Up.Power.avatar.AvatarId;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AvatarServicoAplicacao {

    private final AvatarRepositorioAplicacao avatarRepositorio;
    private final AvatarService avatarDominioService;
    private final ExperienceService experienceService;

    public AvatarServicoAplicacao(
            AvatarRepositorioAplicacao avatarRepositorio,
            AvatarService avatarDominioService,
            ExperienceService experienceService // Injeta o serviço de domínio de experiência
    ) {
        this.avatarRepositorio = avatarRepositorio;
        this.avatarDominioService = avatarDominioService;
        this.experienceService = experienceService;
    }

    public AvatarResumo obterPorPerfilId(Integer perfilId) {
        return avatarRepositorio.obterPorPerfilId(perfilId)
                .map(AvatarResumoAssembler::toResumo)
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
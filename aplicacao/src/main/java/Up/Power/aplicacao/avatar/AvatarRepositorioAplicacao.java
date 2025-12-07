package Up.Power.aplicacao.avatar;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarId;

import java.util.Optional;

public interface AvatarRepositorioAplicacao {
    Optional<Avatar> obterPorId(AvatarId id);
    // Adiciona uma busca simplificada para a camada de aplicação
    Optional<Avatar> obterPorPerfilId(Integer perfilId);
    Avatar salvar(Avatar avatar);
}
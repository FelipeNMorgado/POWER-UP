package Up.Power.aplicacao.avatar;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.perfil.PerfilId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AvatarRepositorioAplicacaoImpl implements AvatarRepositorioAplicacao {

    private final AvatarRepository avatarRepository;

    public AvatarRepositorioAplicacaoImpl(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @Override
    public Optional<Avatar> obterPorId(AvatarId id) {
        return avatarRepository.findById(id);
    }

    @Override
    public Optional<Avatar> obterPorPerfilId(Integer perfilId) {
        // Converte o ID primitivo em PerfilId (Value Object)
        return avatarRepository.findByPerfilId(new PerfilId(perfilId));
    }

    @Override
    public Avatar salvar(Avatar avatar) {
        avatarRepository.save(avatar);
        return avatar;
    }
}
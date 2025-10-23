package Up.Power.avatar;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarRepository;
import java.util.NoSuchElementException;

public class AvatarService {
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) { this.avatarRepository = avatarRepository; }

    private Avatar getAvatar(AvatarId avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new NoSuchElementException("Avatar não encontrado: " + avatarId));
    }

    // Esses metodos serao usandos para fazer o calculo dos atributos fisicos do usuario //
    // A "formula" paara o calculo ainda nao foi totalmente decidida entao esta sendo realizado um calculo//
    // mais basico para a realizacao dos testes //
    public int getForca(AvatarId avatarId) {
        return getAvatar(avatarId).getNivel() * 2; // Exemplo de cálculo
    }

    public int getResistencia(AvatarId avatarId) {
        return getAvatar(avatarId).getNivel() * 3; // Exemplo de cálculo
    }

    public int getAgilidade(AvatarId avatarId) {
        return getAvatar(avatarId).getNivel(); // Exemplo de cálculo
    }
}
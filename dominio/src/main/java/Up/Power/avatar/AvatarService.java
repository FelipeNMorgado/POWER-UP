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


    public int getForca(AvatarId avatarId) {
        return getAvatar(avatarId).getForca(); // Exemplo de cálculo
    }

    public int getResistencia(AvatarId avatarId) {
        return getAvatar(avatarId).getNivel() * 3; // Exemplo de cálculo
    }

    public int getAgilidade(AvatarId avatarId) {
        return getAvatar(avatarId).getNivel(); // Exemplo de cálculo
    }
}
package Up.Power.avatar;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarRepository;
import java.util.NoSuchElementException;

import static java.lang.Math.min;
import static java.lang.Math.round;

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

    public int getAgilidade(AvatarId avatarId) {
        return Math.toIntExact(min(100, round(
                (getAvatar(avatarId).getNivel() / 25.0) * 60.0         // componente do nível (0..60)
                        + (getAvatar(avatarId).getExperiencia() / 1000.0) * 20.0 // componente da experiência (0..20 se exp=1000)
                        + (getAvatar(avatarId).getForca() * 0.2)                 // componente da força (0..20)
        ))); // Exemplo de cálculo
    }

    public int getResistencia(AvatarId avatarId) {
        return Math.toIntExact(min(100, round(
                (getAvatar(avatarId).getForca() * 0.5)                 // componente da força (0..50)
                        + (getAvatar(avatarId).getNivel() / 25.0) * 40.0         // componente do nível (0..40)
                        + (getAvatar(avatarId).getExperiencia() / 1000.0) * 10.0 // componente da experiência (0..10 se exp=1000)
        )))
                ;
    }
}
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
        return Math.toIntExact(min(1000, round(
                (getAvatar(avatarId).getNivel() / 25.0) * 600.0         // componente do nível (0..600)
                        + (getAvatar(avatarId).getExperiencia() / 1000.0) * 200.0 // componente da experiência (0..200 se exp=1000)
                        + (getAvatar(avatarId).getForca() * 0.2)                 // componente da força (0..200 se forca=1000)
        ))); // Exemplo de cálculo
    }

    public int getResistencia(AvatarId avatarId) {
        return Math.toIntExact(min(1000, round(
                (getAvatar(avatarId).getForca() * 0.5)                 // componente da força (0..500 se forca=1000)
                        + (getAvatar(avatarId).getNivel() / 25.0) * 400.0         // componente do nível (0..400)
                        + (getAvatar(avatarId).getExperiencia() / 1000.0) * 100.0 // componente da experiência (0..100 se exp=1000)
        )))
                ;
    }
}
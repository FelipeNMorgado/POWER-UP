package Up.Power.avatar;

import Up.Power.Avatar;
import Up.Power.perfil.PerfilId;

import java.util.Optional;

public interface AvatarRepository {
    void save(Avatar avatar);
    Optional<Avatar> findById(AvatarId id);
    Optional<Avatar> findByPerfilId(PerfilId perfilId);
}
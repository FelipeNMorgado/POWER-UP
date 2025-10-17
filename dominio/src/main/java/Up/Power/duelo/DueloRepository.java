package Up.Power.duelo;

import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;

import java.util.Optional;

public interface DueloRepository {
    Duelo save(Duelo duelo); // Mudado para retornar o Duelo salvo
    Optional<Duelo> findById(DueloId id);
    Optional<Duelo> findLastDuelBetween(AvatarId avatarId1, AvatarId avatarId2);
    
}
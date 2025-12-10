package Up.Power.duelo;

import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DueloRepository {
    Duelo save(Duelo duelo); // Mudado para retornar o Duelo salvo
    Optional<Duelo> findById(DueloId id);
    Optional<Duelo> findLastDuelBetween(AvatarId avatarId1, AvatarId avatarId2);
    List<Duelo> findDuelsBetweenSince(AvatarId avatarId1, AvatarId avatarId2, LocalDateTime dataInicio);
}
package Up.Power.aplicacao.duelo;

import Up.Power.Duelo;
import Up.Power.duelo.DueloId;
import Up.Power.avatar.AvatarId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DueloRepositorioAplicacao {
    Optional<Duelo> obterPorId(DueloId id);
    Optional<Duelo> ultimoEntrePerfis(Integer perfilId1, Integer perfilId2);
    Duelo salvar(Duelo duelo);
    List<Duelo> findDuelsBetweenSince(AvatarId avatarId1, AvatarId avatarId2, LocalDateTime dataInicio);
}
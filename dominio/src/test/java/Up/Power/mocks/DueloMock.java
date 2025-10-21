package Up.Power.mocks;

import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;
import Up.Power.duelo.DueloId;
import Up.Power.duelo.DueloRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DueloMock implements DueloRepository {

    private final Map<DueloId, Duelo> bancoEmMemoria;
    private final AtomicInteger sequence;

    public DueloMock() {
        this.bancoEmMemoria = new HashMap<>();
        this.sequence = new AtomicInteger(0);
    }

    @Override
    public Duelo save(Duelo duelo) {
        if (duelo == null) {
            throw new IllegalArgumentException("Duelo não pode ser nulo.");
        }

        if (duelo.getId() == null) {
            int proximoId = sequence.incrementAndGet();
            DueloId novoId = new DueloId(proximoId);

            duelo.setId(novoId);
            bancoEmMemoria.put(novoId, duelo);
        } else {
            bancoEmMemoria.put(duelo.getId(), duelo);
        }

        return duelo;
    }

    @Override
    public Optional<Duelo> findById(DueloId id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    @Override
    public Optional<Duelo> findLastDuelBetween(AvatarId avatarId1, AvatarId avatarId2) {
        return bancoEmMemoria.values().stream()

                .filter(duelo ->
                        (duelo.getAvatar1().equals(avatarId1) && duelo.getAvatar2().equals(avatarId2)) ||
                                (duelo.getAvatar1().equals(avatarId2) && duelo.getAvatar2().equals(avatarId1))
                )

                .max(Comparator.comparing(Duelo::getDataDuelo));
    }

    public List<Duelo> findAll() {
        return new ArrayList<>(bancoEmMemoria.values());
    }

    public void deleteAll() {
        bancoEmMemoria.clear();
        sequence.set(0);
    }
}
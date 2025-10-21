package Up.Power.mocks;

import Up.Power.Rivalidade;
import Up.Power.duelo.DueloId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeRepository;
import Up.Power.rivalidade.StatusRivalidade;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RivalidadeMock implements RivalidadeRepository {

    private final Map<RivalidadeId, Rivalidade> bancoEmMemoria;

    private final AtomicInteger sequence;

    public RivalidadeMock() {
        this.bancoEmMemoria = new HashMap<>();
        this.sequence = new AtomicInteger(0);
    }

    @Override
    public boolean existsActiveRivalryForPerfil(PerfilId perfilId) {
        for (Rivalidade r : bancoEmMemoria.values()) {
            if (r.getStatus() == StatusRivalidade.ATIVA) {
                if (r.getPerfil1().equals(perfilId) || r.getPerfil2().equals(perfilId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Rivalidade save(Rivalidade rivalidade) {
        if (rivalidade == null) {
            throw new IllegalArgumentException("Rivalidade não pode ser nula.");
        }

        if (rivalidade.getId() == null) {
            int proximoId = sequence.incrementAndGet();
            RivalidadeId novoId = new RivalidadeId(proximoId);

            rivalidade.setId(novoId);
            bancoEmMemoria.put(novoId, rivalidade);
        } else {
            bancoEmMemoria.put(rivalidade.getId(), rivalidade);
        }

        return rivalidade;
    }

    @Override
    public Optional<Rivalidade> findById(RivalidadeId id) {
        Rivalidade rivalidadeEncontrada = bancoEmMemoria.get(id);
        return Optional.ofNullable(rivalidadeEncontrada);
    }

    public List<Rivalidade> findAll() {
        return new ArrayList<>(bancoEmMemoria.values());
    }

    public void deleteAll() {
        bancoEmMemoria.clear();
        sequence.set(0);
    }
}
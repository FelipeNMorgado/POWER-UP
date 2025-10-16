package Up.Power.mocks;


import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PerfilMock implements PerfilRepository {

    private final Map<PerfilId, Perfil> bancoEmMemoria;

    public PerfilMock() {
        this.bancoEmMemoria = new HashMap<>();
    }

    @Override
    public Perfil save(Perfil perfil) {
        if (perfil == null || perfil.getId() == null) {
            throw new IllegalArgumentException("Perfil e ID do Perfil não podem ser nulos.");
        }
        bancoEmMemoria.put(perfil.getId(), perfil);
        return perfil;
    }

    @Override
    public Optional<Perfil> findById(PerfilId id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    public void deleteAll() {
        bancoEmMemoria.clear();
    }
}
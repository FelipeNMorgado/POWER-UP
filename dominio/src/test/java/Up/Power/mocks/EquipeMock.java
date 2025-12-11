package Up.Power.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Up.Power.Equipe;
import Up.Power.equipe.EquipeId;
import Up.Power.equipe.EquipeRepository;
import Up.Power.perfil.PerfilId;

public class EquipeMock implements EquipeRepository {

    private final Map<Integer, Equipe> equipesPorId = new HashMap<>();

    @Override
    public void salvar(Equipe equipe) {
        if (equipe == null || equipe.getId() == null) {
            throw new IllegalArgumentException("Equipe inválida");
        }
        equipesPorId.put(equipe.getId().getId(), equipe);
    }

    @Override
    public List<Equipe> listarEquipe(EquipeId id, PerfilId perfil) {
        if (id != null) {
            Equipe equipe = equipesPorId.get(id.getId());
            return equipe == null ? Collections.emptyList() : Collections.singletonList(equipe);
        }
        return new ArrayList<>(equipesPorId.values());
    }

    @Override
    public void excluir(EquipeId id) {
        if (id != null) {
            equipesPorId.remove(id.getId());
        }
    }

}



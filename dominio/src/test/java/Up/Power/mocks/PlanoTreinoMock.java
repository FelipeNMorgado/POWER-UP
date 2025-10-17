package Up.Power.mocks;

import Up.Power.PlanoTreino;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;

import java.util.*;

public class PlanoTreinoMock implements PlanoTreinoRepository {

    private final Map<Integer, PlanoTreino> storage = new HashMap<>();

    @Override
    public void salvar(PlanoTreino planoT) {
        // Regra de validação mínima: deve ter ao menos um exercício
        if (planoT.getTreinos() == null || planoT.getTreinos().isEmpty()) {
            throw new IllegalArgumentException("Plano de treino deve conter ao menos um exercício");
        }
        storage.put(planoT.getId().getId(), planoT);
    }

    @Override
    public void excluir(PlanoTId planoT) {
        storage.remove(planoT.getId());
    }

    @Override
    public List<PlanoTreino> listar(PlanoTId planoT) {
        if (planoT == null) {
            return new ArrayList<>(storage.values());
        }
        PlanoTreino existente = storage.get(planoT.getId());
        if (existente == null) return Collections.emptyList();
        return Collections.singletonList(existente);
    }

    // Helpers para testes
    public Optional<PlanoTreino> obter(PlanoTId id) {
        return Optional.ofNullable(storage.get(id.getId()));
    }

    public void limpar() {
        storage.clear();
    }
}



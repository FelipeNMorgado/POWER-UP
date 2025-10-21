package Up.Power.mocks;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.conquista.ConquistaRepository;

import java.util.*;

public class ConquistaMock implements ConquistaRepository {

    private final Map<ConquistaId, Conquista> ativas = new HashMap<>();
    private final Map<ConquistaId, Conquista> concluidas = new HashMap<>();

    @Override
    public void salvar(Conquista conquista) {
        ativas.put(conquista.getId(), conquista);
    }

    @Override
    public void marcarComoConcluida(Conquista conquista) {
        ativas.remove(conquista.getId());
        concluidas.put(conquista.getId(), conquista);
    }

    @Override
    public Optional<Conquista> buscarPorId(ConquistaId id) {
        if (ativas.containsKey(id)) return Optional.of(ativas.get(id));
        if (concluidas.containsKey(id)) return Optional.of(concluidas.get(id));
        return Optional.empty();
    }

    @Override
    public List<Conquista> listarAtivas() {
        return new ArrayList<>(ativas.values());
    }

    @Override
    public List<Conquista> listarConcluidas() {
        return new ArrayList<>(concluidas.values());
    }

    @Override
    public void remover(ConquistaId id) {
        ativas.remove(id);
        concluidas.remove(id);
    }
}

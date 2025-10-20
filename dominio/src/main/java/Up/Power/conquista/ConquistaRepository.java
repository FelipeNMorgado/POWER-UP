package Up.Power.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import java.util.List;
import java.util.Optional;

public interface ConquistaRepository {
    void salvar(Conquista conquista);
    void marcarComoConcluida(Conquista conquista);
    Optional<Conquista> buscarPorId(ConquistaId id);
    List<Conquista> listarAtivas();
    List<Conquista> listarConcluidas();
    void remover(ConquistaId id);
}

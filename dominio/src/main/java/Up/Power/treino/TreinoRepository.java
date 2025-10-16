package Up.Power.treino;

import Up.Power.Treino;

import java.util.List;

public interface TreinoRepository {
    void salvar(Treino treino);
    List<TreinoId> listar(TreinoId treino);
}

package Up.Power;

import java.util.List;

public interface TreinoRepository {
    void salvar(Treino treino);
    List<TreinoId> listar(TreinoId treino);
}

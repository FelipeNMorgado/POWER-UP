package Up.Power;

import java.util.List;

public interface PlanoTreinoRepository {
    void salvar(PlanoTreino planoT);
    void excluir(PlanoTId planoT);
    List<PlanoTreino> listar(PlanoTId planoT);
}

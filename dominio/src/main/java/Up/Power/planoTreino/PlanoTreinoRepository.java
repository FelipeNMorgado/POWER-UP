package Up.Power.planoTreino;

import Up.Power.PlanoTreino;

import java.util.List;

public interface PlanoTreinoRepository {
    void salvar(PlanoTreino planoT);
    void excluir(PlanoTId planoT);
    List<PlanoTreino> listar(PlanoTId planoT);
}

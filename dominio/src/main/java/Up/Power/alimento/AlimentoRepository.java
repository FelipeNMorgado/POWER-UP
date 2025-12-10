package Up.Power.alimento;

import Up.Power.Alimento;

public interface AlimentoRepository {
    void salvar(Alimento alimento);
    java.util.List<Alimento> listarTodos();
}



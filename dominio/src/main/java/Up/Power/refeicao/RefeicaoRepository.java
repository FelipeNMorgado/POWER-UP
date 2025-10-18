package Up.Power.refeicao;

import Up.Power.Refeicao;

import java.util.List;

public interface RefeicaoRepository {
    void salvar(Refeicao refeicao);
    void editar(RefeicaoId refeicao);
    void excluir(RefeicaoId refeicao);
    Refeicao obter(RefeicaoId refeicao);
    List<Refeicao> listar(RefeicaoId refeicao);
}
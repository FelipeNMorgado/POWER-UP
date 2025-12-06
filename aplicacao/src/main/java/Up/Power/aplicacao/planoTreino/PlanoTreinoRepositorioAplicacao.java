package Up.Power.aplicacao.planoTreino;

import Up.Power.PlanoTreino;
import Up.Power.planoTreino.PlanoTId;

import java.util.List;
import java.util.Optional;

public interface PlanoTreinoRepositorioAplicacao {
    Optional<PlanoTreino> obterPorId(PlanoTId id);
    List<PlanoTreino> listarPorUsuario(String usuarioEmail);
    PlanoTreino salvar(PlanoTreino plano);
    void excluir(PlanoTId id);
}


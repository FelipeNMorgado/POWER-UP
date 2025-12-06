package Up.Power.aplicacao.equipe;

import Up.Power.Equipe;
import Up.Power.equipe.EquipeId;

import java.util.List;
import java.util.Optional;

public interface EquipeRepositorioAplicacao {
    Optional<Equipe> obterPorId(EquipeId id);
    List<Equipe> listarPorUsuario(String usuarioEmail);
    Equipe salvar(Equipe equipe);
    void excluir(EquipeId id);
}


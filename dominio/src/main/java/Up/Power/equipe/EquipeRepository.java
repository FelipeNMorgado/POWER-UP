package Up.Power.equipe;

import Up.Power.Equipe;
import Up.Power.perfil.PerfilId;

import java.util.List;

public interface EquipeRepository {
    void salvar(Equipe equipe);
    List<Equipe> listarEquipe(EquipeId id, PerfilId perfil);
    void excluir(EquipeId id);
}



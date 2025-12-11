package Up.Power.treinoProgresso;

import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;

import java.util.List;

public interface TreinoProgressoRepository {
    TreinoProgresso salvar(TreinoProgresso progresso);
    List<TreinoProgresso> listarPorPerfil(PerfilId perfilId);
    List<TreinoProgresso> listarPorPerfilEExercicio(PerfilId perfilId, ExercicioId exercicioId);
    void deletar(TreinoProgressoId id);
}


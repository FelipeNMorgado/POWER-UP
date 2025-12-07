package Up.Power.aplicacao.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

public class ConquistaResumoAssembler {

    public ConquistaResumo toResumo(Conquista conquista, boolean concluida, String badgeAtual) {
        return new ConquistaResumo(
                conquista.getId().getId(),
                conquista.getExercicio().getId(),
                conquista.getTreino().getId(),
                conquista.getNome(),
                conquista.getDescricao(),
                concluida,
                badgeAtual
        );
    }
}
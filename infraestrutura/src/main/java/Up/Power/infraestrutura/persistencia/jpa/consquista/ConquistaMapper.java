package Up.Power.infraestrutura.persistencia.jpa.consquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

public class ConquistaMapper {

    public static ConquistaJpa toEntity(Conquista c, boolean concluida) {
        return new ConquistaJpa(
                c.getId() == null ? null : c.getId().getId(),
                c.getExercicio().getId(),
                c.getTreino().getId(),
                c.getDescricao(),
                c.getNome(),
                concluida
        );
    }

    public static Conquista toDomain(ConquistaJpa e) {
        return new Conquista(
                new ConquistaId(e.getId()),
                new ExercicioId(e.getExercicioId()),
                new TreinoId(e.getTreinoId()),
                e.getDescricao(),
                e.getNome()
        );
    }
}
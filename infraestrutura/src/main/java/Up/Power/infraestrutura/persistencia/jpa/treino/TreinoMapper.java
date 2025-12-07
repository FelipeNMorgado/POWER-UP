package Up.Power.infraestrutura.persistencia.jpa.treino;

import Up.Power.Treino;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

public class TreinoMapper {

    // DOMAIN → ENTITY
    public static TreinoJpa toEntity(Treino domain) {
        return new TreinoJpa(
                domain.getId().getId(),
                domain.getExercicio().getId(),
                domain.getTipo(),
                domain.getTempo(),
                domain.getDistancia(),
                domain.getRepeticoes(),
                domain.getPeso(),
                domain.getSeries(),
                domain.getRecordeCarga()
        );
    }

    // ENTITY → DOMAIN
    public static Treino toDomain(TreinoJpa entity) {

        Treino domain = new Treino(
                new TreinoId(entity.getId()),
                new ExercicioId(entity.getExercicioId()),
                entity.getTipo()
        );

        domain.setTempo(entity.getTempo());
        domain.setDistancia(entity.getDistancia());
        domain.setRepeticoes(entity.getRepeticoes());
        domain.setPeso(entity.getPeso());
        domain.setSeries(entity.getSeries());
        domain.setRecordeCarga(entity.getRecordeCarga());

        return domain;
    }
}

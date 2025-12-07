package Up.Power.infraestrutura.persistencia.jpa.meta;

import Up.Power.Meta;
import Up.Power.exercicio.ExercicioId;
import Up.Power.meta.MetaId;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Component;

@Component
public class MetaMapper {

    public MetaJpa toEntity(Meta meta) {
        return new MetaJpa(
                meta.getId() != null ? meta.getId().getId() : null,
                meta.getExercicio() != null ? meta.getExercicio().getId() : null,
                meta.getTreino() != null ? meta.getTreino().getId() : null,
                meta.getNome(),
                meta.getInicio(),
                meta.getFim()
        );
    }

    public Meta toDomain(MetaJpa entity) {
        return new Meta(
                new MetaId(entity.getId()),
                entity.getExercicioId() != null ? new ExercicioId(entity.getExercicioId()) : null,
                entity.getTreinoId() != null ? new TreinoId(entity.getTreinoId()) : null,
                entity.getNome(),
                entity.getFim(),
                entity.getInicio()
        );
    }
}


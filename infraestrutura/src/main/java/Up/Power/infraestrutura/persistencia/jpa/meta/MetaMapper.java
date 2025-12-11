package Up.Power.infraestrutura.persistencia.jpa.meta;

import Up.Power.Meta;
import Up.Power.exercicio.ExercicioId;
import Up.Power.meta.MetaId;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Component;

@Component
public class MetaMapper {

    public MetaJpa toEntity(Meta meta) {
        // Se o ID for 0 ou null, usar null para que o JPA gere o ID automaticamente (INSERT)
        // Se o ID for > 0, usar o ID para UPDATE
        Integer id = (meta.getId() == null || meta.getId().getId() == 0) 
            ? null 
            : meta.getId().getId();
        
        MetaJpa jpa = new MetaJpa(
                id,
                meta.getExercicio() != null ? meta.getExercicio().getId() : null,
                meta.getTreino() != null ? meta.getTreino().getId() : null,
                meta.getNome(),
                meta.getInicio(),
                meta.getFim(),
                meta.getExigenciaMinima()
        );
        return jpa;
    }

    public Meta toDomain(MetaJpa entity) {
        return new Meta(
                new MetaId(entity.getId()),
                entity.getExercicioId() != null ? new ExercicioId(entity.getExercicioId()) : null,
                entity.getTreinoId() != null ? new TreinoId(entity.getTreinoId()) : null,
                entity.getNome(),
                entity.getFim(),
                entity.getInicio(),
                entity.getExigenciaMinima()
        );
    }
}


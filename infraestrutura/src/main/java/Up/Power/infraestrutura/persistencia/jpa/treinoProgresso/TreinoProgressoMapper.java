package Up.Power.infraestrutura.persistencia.jpa.treinoProgresso;

import Up.Power.treinoProgresso.TreinoProgresso;
import Up.Power.treinoProgresso.TreinoProgressoId;
import Up.Power.perfil.PerfilId;
import Up.Power.exercicio.ExercicioId;

public class TreinoProgressoMapper {

    public static TreinoProgressoJpa toEntity(TreinoProgresso domain) {
        TreinoProgressoJpa entity = new TreinoProgressoJpa();
        entity.setId(domain.getId() != null ? domain.getId().getId() : null);
        entity.setPerfilId(domain.getPerfilId().getId());
        entity.setExercicioId(domain.getExercicioId().getId());
        entity.setDataRegistro(domain.getDataRegistro());
        entity.setPesoKg(domain.getPesoKg());
        entity.setRepeticoes(domain.getRepeticoes());
        entity.setSeries(domain.getSeries());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public static TreinoProgresso toDomain(TreinoProgressoJpa entity) {
        return new TreinoProgresso(
                new TreinoProgressoId(entity.getId()),
                new PerfilId(entity.getPerfilId()),
                new ExercicioId(entity.getExercicioId()),
                entity.getDataRegistro(),
                entity.getPesoKg(),
                entity.getRepeticoes(),
                entity.getSeries(),
                entity.getCreatedAt()
        );
    }
}


package Up.Power.infraestrutura.persistencia.jpa.treino;

import Up.Power.Treino;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

public class TreinoMapper {

    // DOMAIN → ENTITY
    public static TreinoJpa toEntity(Treino domain) {
        // Se o ID for 0 ou null, passar null para que o banco gere o ID automaticamente
        Integer id = (domain.getId() != null && domain.getId().getId() != 0) 
                ? domain.getId().getId() 
                : null;
        
        TreinoJpa entity = new TreinoJpa();
        entity.setId(id); // null para novos treinos
        entity.setExercicioId(domain.getExercicio().getId());
        entity.setTipo(domain.getTipo());
        entity.setTempo(domain.getTempo());
        entity.setDistancia(domain.getDistancia());
        entity.setRepeticoes(domain.getRepeticoes());
        entity.setPeso(domain.getPeso());
        entity.setSeries(domain.getSeries());
        entity.setRecordeCarga(domain.getRecordeCarga());
        
        return entity;
    }

    // ENTITY → DOMAIN
    public static Treino toDomain(TreinoJpa entity) {
        // Se o ID for null, usar 0 (será tratado como novo treino)
        int treinoId = (entity.getId() != null) ? entity.getId() : 0;
        Treino domain = new Treino(
                new TreinoId(treinoId),
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

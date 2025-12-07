package Up.Power.infraestrutura.persistencia.jpa.exercicio;

import Up.Power.Exercicio;
import Up.Power.exercicio.ExercicioId;
import org.springframework.stereotype.Component;

@Component
public class ExercicioMapper {

    public ExercicioJpa toEntity(Exercicio exercicio) {
        // Exercicio.getId(int) retorna ExercicioId, então precisamos obter o id interno
        Integer id = null;
        ExercicioId exercicioId = exercicio.getId(0);
        if (exercicioId != null) {
            id = exercicioId.getId();
        }
        return new ExercicioJpa(id, exercicio.getNome());
    }

    public Exercicio toDomain(ExercicioJpa entity) {
        return new Exercicio(
                new ExercicioId(entity.getId()),
                entity.getNome()
        );
    }
}


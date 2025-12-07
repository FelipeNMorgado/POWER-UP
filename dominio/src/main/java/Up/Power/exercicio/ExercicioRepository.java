package Up.Power.exercicio;

import Up.Power.Exercicio;

import java.util.Optional;

public interface ExercicioRepository {
    void salvar(Exercicio exercicio);
    Optional<Exercicio> obter(ExercicioId id);
}


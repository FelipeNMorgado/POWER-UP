package Up.Power.acessorio;

import Up.Power.Acessorio;

import java.util.List;
import java.util.Optional;

public interface AcessorioRepository {
    void save(Acessorio acessorio);
    Optional<Acessorio> findById(Acessorio id);
    Optional<Acessorio> findById(AcessorioId id);
    List<Acessorio> findAll();
}

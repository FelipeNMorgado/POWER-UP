package Up.Power.rivalidade;

import Up.Power.Rivalidade;
import Up.Power.perfil.PerfilId;

import java.util.Optional;

public interface RivalidadeRepository {
    Rivalidade save(Rivalidade rivalidade);
    Optional<Rivalidade> findById(RivalidadeId id);
    boolean existsActiveRivalryForPerfil(PerfilId perfilId);

}

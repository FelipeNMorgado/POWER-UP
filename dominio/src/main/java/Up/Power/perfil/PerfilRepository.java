package Up.Power.perfil;

import Up.Power.Perfil;

import java.util.Optional;

public interface PerfilRepository {
    Optional<Perfil> findById(PerfilId id);
    Perfil save(Perfil perfil);

}



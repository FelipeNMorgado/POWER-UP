package Up.Power.infraestrutura.persistencia.jpa.perfil;

import Up.Power.Email;
import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PerfilRepositoryImpl implements PerfilRepository {

    private final JpaPerfilRepository repo;
    private final PerfilMapper mapper;

    public PerfilRepositoryImpl(JpaPerfilRepository repo, PerfilMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Perfil> findById(PerfilId id) {
        return repo.findById(id.getId())
                .map(mapper::toDomain);
    }

    @Override
    public Perfil save(Perfil perfil) {
        PerfilJpa entity = mapper.toEntity(perfil);
        PerfilJpa saved = repo.save(entity);
        return mapper.toDomain(saved);
    }

    public Optional<Perfil> findByUsuarioEmail(String usuarioEmail) {
        return repo.findByUsuarioEmail(usuarioEmail)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        // Implementação simplificada: verifica se ambos os perfis existem
        // A lógica completa de amizade pode ser implementada via query específica
        Optional<Perfil> perfil1 = findById(perfilId1);
        Optional<Perfil> perfil2 = findById(perfilId2);
        
        if (perfil1.isEmpty() || perfil2.isEmpty()) {
            return false;
        }
        
        // Verifica se são amigos (verifica na lista de amigos de cada perfil)
        return perfil1.get().getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfil2.get().getUsuarioEmail()))
                || perfil2.get().getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfil1.get().getUsuarioEmail()));
    }
}


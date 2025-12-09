package Up.Power.infraestrutura.persistencia.jpa.perfil;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;

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
        // Verifica existência de amizade diretamente na tabela de join para evitar carregar coleções
        if (perfilId1 == null || perfilId2 == null) {
            return false;
        }

        Integer id1 = perfilId1.getId();
        Integer id2 = perfilId2.getId();

        if (id1 == null || id2 == null) {
            return false;
        }

        // Checa em ambas as direções (perfil -> amigo) para garantir compatibilidade
        boolean direct = repo.existsFriendship(id1, id2);
        boolean reverse = repo.existsFriendship(id2, id1);
        return direct || reverse;
    }
}


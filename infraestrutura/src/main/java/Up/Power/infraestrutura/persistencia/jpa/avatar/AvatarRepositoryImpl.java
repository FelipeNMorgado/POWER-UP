package Up.Power.infraestrutura.persistencia.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import Up.Power.Avatar;
import Up.Power.avatar.AvatarId;

import java.util.Optional;

@Repository
class AvatarRepositorioImpl {

    private final AvatarJpaRepository repositorio;

    @Autowired
    public AvatarRepositorioImpl(AvatarJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Salvar (domínio → JPA)
    public void salvar(Avatar avatar) {
        AvatarJpa jpa = AvatarJpa.fromDomain(avatar);
        repositorio.save(jpa);
    }

    // Obter (JPA → domínio)
    public Optional<Avatar> obter(AvatarId id) {
        int intId = Integer.parseInt(id.toString());
        return repositorio.findById(intId).map(AvatarJpa::toDomain);
    }
}
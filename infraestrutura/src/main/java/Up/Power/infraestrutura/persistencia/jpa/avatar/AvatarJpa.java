package Up.Power.infraestrutura.persistencia.jpa.avatar;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Up.Power.Avatar;
import Up.Power.acessorio.AcessorioId;
import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.perfil.PerfilId;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "avatar")
public class AvatarJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    @ElementCollection
    @CollectionTable(
            name = "avatar_acessorios",
            joinColumns = @JoinColumn(name = "avatar_id")
    )
    @Column(name = "acessorio_id")
    private List<Integer> acessorioIds;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private Integer experiencia;

    @Column(nullable = false)
    private Integer dinheiro;

    @Column(nullable = false)
    private Integer forca;

    public AvatarJpa() {}

    public AvatarJpa(Integer id, Integer perfilId, List<Integer> acessorioIds,
                     Integer nivel, Integer experiencia, Integer dinheiro, Integer forca) {
        this.id = id;
        this.perfilId = perfilId;
        this.acessorioIds = acessorioIds;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.dinheiro = dinheiro;
        this.forca = forca;
    }

    // Getters
    public Integer getId() { return id; }
    public Integer getPerfilId() { return perfilId; }
    public List<Integer> getAcessorioIds() { return acessorioIds; }
    public Integer getNivel() { return nivel; }
    public Integer getExperiencia() { return experiencia; }
    public Integer getDinheiro() { return dinheiro; }
    public Integer getForca() { return forca; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setPerfilId(Integer perfilId) { this.perfilId = perfilId; }
    public void setAcessorioIds(List<Integer> acessorioIds) { this.acessorioIds = acessorioIds; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }
    public void setDinheiro(Integer dinheiro) { this.dinheiro = dinheiro; }
    public void setForca(Integer forca) { this.forca = forca; }
}

interface JpaAvatarRepository extends JpaRepository<AvatarJpa, Integer> {
    Optional<AvatarJpa> findByPerfilId(Integer perfilId);
}

@Repository
class AvatarRepositoryImpl implements AvatarRepository {

    private final JpaAvatarRepository repo;

    public AvatarRepositoryImpl(JpaAvatarRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(Avatar avatar) {
        AvatarJpa entity = AvatarMapper.toEntity(avatar);
        repo.save(entity);
    }

    @Override
    public Optional<Avatar> findById(AvatarId id) {
        return repo.findById(id.getId())
                .map(AvatarMapper::toDomain);
    }

    @Override
    public Optional<Avatar> findByPerfilId(PerfilId perfilId) {
        return repo.findByPerfilId(perfilId.getId())
                .map(AvatarMapper::toDomain);
    }
}


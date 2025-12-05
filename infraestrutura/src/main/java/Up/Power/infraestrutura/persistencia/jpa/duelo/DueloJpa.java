package Up.Power.infraestrutura.persistencia.jpa.duelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;
import Up.Power.duelo.DueloId;
import Up.Power.duelo.DueloRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Entity
@Table(name = "duelo")
public class DueloJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "avatar1_id", nullable = false)
    private Integer avatar1Id;

    @Column(name = "avatar2_id", nullable = false)
    private Integer avatar2Id;

    @Column(nullable = false)
    private String resultado;

    @Column(name = "data_duelo", nullable = false)
    private LocalDateTime dataDuelo;

    public DueloJpa() {}

    public DueloJpa(Integer id, Integer avatar1Id, Integer avatar2Id, String resultado, LocalDateTime dataDuelo) {
        this.id = id;
        this.avatar1Id = avatar1Id;
        this.avatar2Id = avatar2Id;
        this.resultado = resultado;
        this.dataDuelo = dataDuelo;
    }

    public Integer getId() { return id; }
    public Integer getAvatar1Id() { return avatar1Id; }
    public Integer getAvatar2Id() { return avatar2Id; }
    public String getResultado() { return resultado; }
    public LocalDateTime getDataDuelo() { return dataDuelo; }

    public void setId(Integer id) { this.id = id; }
    public void setAvatar1Id(Integer avatar1Id) { this.avatar1Id = avatar1Id; }
    public void setAvatar2Id(Integer avatar2Id) { this.avatar2Id = avatar2Id; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public void setDataDuelo(LocalDateTime dataDuelo) { this.dataDuelo = dataDuelo; }
}


interface JpaDueloRepository extends JpaRepository<DueloJpa, Integer> {

    @Query("""
        SELECT d FROM DueloJpa d
        WHERE (d.avatar1Id = :a AND d.avatar2Id = :b)
           OR (d.avatar1Id = :b AND d.avatar2Id = :a)
        ORDER BY d.dataDuelo DESC
        LIMIT 1
    """)
    Optional<DueloJpa> findLastDuelBetween(Integer a, Integer b);
}


@Repository
class DueloRepositoryJpa implements DueloRepository {

    private final JpaDueloRepository repo;

    public DueloRepositoryJpa(JpaDueloRepository repo) {
        this.repo = repo;
    }

    @Override
    public Duelo save(Duelo duelo) {
        DueloJpa entity = DueloMapper.toEntity(duelo);
        DueloJpa saved = repo.save(entity);
        return DueloMapper.toDomain(saved);
    }

    @Override
    public Optional<Duelo> findById(DueloId id) {
        return repo.findById(id.getId())
                .map(DueloMapper::toDomain);
    }

    @Override
    public Optional<Duelo> findLastDuelBetween(AvatarId avatarId1, AvatarId avatarId2) {
        return repo.findLastDuelBetween(
                avatarId1.getId(),
                avatarId2.getId()
        ).map(DueloMapper::toDomain);
    }
}

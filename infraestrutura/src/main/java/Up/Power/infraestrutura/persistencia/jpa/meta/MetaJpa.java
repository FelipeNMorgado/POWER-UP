package Up.Power.infraestrutura.persistencia.jpa.meta;

import Up.Power.Meta;
import Up.Power.exercicio.ExercicioId;
import Up.Power.meta.MetaId;
import Up.Power.meta.MetaRepository;
import Up.Power.treino.TreinoId;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// ===========================
// ======== ENTITY ===========
// ===========================

@Entity
@Table(name = "meta")
public class MetaJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "exercicio_id")
    private Integer exercicioId;

    @Column(name = "treino_id")
    private Integer treinoId;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;

    @Column(name = "data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fim;

    public MetaJpa() {}

    public MetaJpa(Integer id, Integer exercicioId, Integer treinoId,
                   String nome, Date inicio, Date fim) {
        this.id = id;
        this.exercicioId = exercicioId;
        this.treinoId = treinoId;
        this.nome = nome;
        this.inicio = inicio;
        this.fim = fim;
    }

    // Getters
    public Integer getId() { return id; }
    public Integer getExercicioId() { return exercicioId; }
    public Integer getTreinoId() { return treinoId; }
    public String getNome() { return nome; }
    public Date getInicio() { return inicio; }
    public Date getFim() { return fim; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setExercicioId(Integer exercicioId) { this.exercicioId = exercicioId; }
    public void setTreinoId(Integer treinoId) { this.treinoId = treinoId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setInicio(Date inicio) { this.inicio = inicio; }
    public void setFim(Date fim) { this.fim = fim; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaMetaRepository extends JpaRepository<MetaJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class MetaRepositoryImpl implements MetaRepository {

    private final JpaMetaRepository repo;
    private final MetaMapper mapper;

    public MetaRepositoryImpl(JpaMetaRepository repo, MetaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Meta save(Meta meta) {
        MetaJpa entity = mapper.toEntity(meta);
        MetaJpa saved = repo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Meta> findById(MetaId id) {
        return repo.findById(id.getId())
                .map(mapper::toDomain);
    }

    @Override
    public List<Meta> findByUserId(int userId) {
        // Como Meta não tem userId, retorna todas as metas por enquanto
        // Isso pode ser ajustado no futuro se necessário
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        repo.deleteAll();
    }
}


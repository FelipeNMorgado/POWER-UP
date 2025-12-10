package Up.Power.infraestrutura.persistencia.jpa.exercicio;

import Up.Power.Exercicio;
import Up.Power.exercicio.ExercicioId;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ===========================
// ======== ENTITY ===========
// ===========================

@Entity
@Table(name = "exercicio")
public class ExercicioJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    public ExercicioJpa() {}

    public ExercicioJpa(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters
    public Integer getId() { return id; }
    public String getNome() { return nome; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaExercicioRepository extends JpaRepository<ExercicioJpa, Integer> {
    Optional<ExercicioJpa> findByNome(String nome);
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class ExercicioRepositoryImpl implements Up.Power.exercicio.ExercicioRepository {

    private final JpaExercicioRepository repo;
    private final ExercicioMapper mapper;

    public ExercicioRepositoryImpl(JpaExercicioRepository repo, ExercicioMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Exercicio exercicio) {
        ExercicioJpa entity = mapper.toEntity(exercicio);
        repo.save(entity);
    }

    @Override
    public Optional<Exercicio> obter(ExercicioId id) {
        return repo.findById(id.getId())
                .map(mapper::toDomain);
    }

    @Override
    public List<Exercicio> listarTodos() {
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}


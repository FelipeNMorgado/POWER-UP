package Up.Power.infraestrutura.persistencia.jpa.alimento;

import Up.Power.Alimento;
import Up.Power.alimento.AlimentoId;
import Up.Power.alimento.AlimentoRepository;
import Up.Power.alimento.Categoria;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================
// ======== ENTITY ===========
// ===========================

@Entity
@Table(name = "alimento")
public class AlimentoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer calorias;

    @Column(nullable = false)
    private Float quantidade;

    public AlimentoJpa() {}

    public AlimentoJpa(Integer id, Categoria categoria, String nome, Integer calorias, Float quantidade) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.calorias = calorias;
        this.quantidade = quantidade;
    }

    // Getters
    public Integer getId() { return id; }
    public Categoria getCategoria() { return categoria; }
    public String getNome() { return nome; }
    public Integer getCalorias() { return calorias; }
    public Float getQuantidade() { return quantidade; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCalorias(Integer calorias) { this.calorias = calorias; }
    public void setQuantidade(Float quantidade) { this.quantidade = quantidade; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaAlimentoRepository extends JpaRepository<AlimentoJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class AlimentoRepositoryImpl implements AlimentoRepository {

    private final JpaAlimentoRepository repo;
    private final AlimentoMapper mapper;

    public AlimentoRepositoryImpl(JpaAlimentoRepository repo, AlimentoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Alimento alimento) {
        AlimentoJpa entity = mapper.toEntity(alimento);
        repo.save(entity);
    }
}


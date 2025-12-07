package Up.Power.infraestrutura.persistencia.jpa.refeicao;

import Up.Power.Refeicao;
import Up.Power.alimento.AlimentoId;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.refeicao.RefeicaoRepository;
import Up.Power.refeicao.TipoRefeicao;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// ===========================
// ======== ENTITY ===========
// ===========================

@Entity
@Table(name = "refeicao")
public class RefeicaoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRefeicao tipo;

    @ElementCollection
    @CollectionTable(
            name = "refeicao_alimentos",
            joinColumns = @JoinColumn(name = "refeicao_id")
    )
    @Column(name = "alimento_id")
    private List<Integer> alimentosIds;

    @Column(name = "calorias_totais")
    private Integer caloriasTotais;

    @Column(name = "data_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;

    @Column(name = "data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fim;

    public RefeicaoJpa() {}

    public RefeicaoJpa(Integer id, TipoRefeicao tipo, List<Integer> alimentosIds,
                       Integer caloriasTotais, Date inicio, Date fim) {
        this.id = id;
        this.tipo = tipo;
        this.alimentosIds = alimentosIds;
        this.caloriasTotais = caloriasTotais;
        this.inicio = inicio;
        this.fim = fim;
    }

    // Getters
    public Integer getId() { return id; }
    public TipoRefeicao getTipo() { return tipo; }
    public List<Integer> getAlimentosIds() { return alimentosIds; }
    public Integer getCaloriasTotais() { return caloriasTotais; }
    public Date getInicio() { return inicio; }
    public Date getFim() { return fim; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setTipo(TipoRefeicao tipo) { this.tipo = tipo; }
    public void setAlimentosIds(List<Integer> alimentosIds) { this.alimentosIds = alimentosIds; }
    public void setCaloriasTotais(Integer caloriasTotais) { this.caloriasTotais = caloriasTotais; }
    public void setInicio(Date inicio) { this.inicio = inicio; }
    public void setFim(Date fim) { this.fim = fim; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaRefeicaoRepository extends JpaRepository<RefeicaoJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class RefeicaoRepositoryImpl implements RefeicaoRepository {

    private final JpaRefeicaoRepository repo;
    private final RefeicaoMapper mapper;

    public RefeicaoRepositoryImpl(JpaRefeicaoRepository repo, RefeicaoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Refeicao refeicao) {
        RefeicaoJpa entity = mapper.toEntity(refeicao);
        repo.save(entity);
    }

    @Override
    public void editar(RefeicaoId refeicaoId) {
        // Implementação de edição se necessário
    }

    @Override
    public void excluir(RefeicaoId refeicaoId) {
        repo.deleteById(refeicaoId.getId());
    }

    @Override
    public Refeicao obter(RefeicaoId refeicaoId) {
        return repo.findById(refeicaoId.getId())
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Refeicao> listar(RefeicaoId refeicaoId) {
        if (refeicaoId != null) {
            return repo.findById(refeicaoId.getId())
                    .map(mapper::toDomain)
                    .map(List::of)
                    .orElse(List.of());
        }
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}


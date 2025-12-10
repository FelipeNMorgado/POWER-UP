package Up.Power.infraestrutura.persistencia.jpa.refeicao;

import Up.Power.Refeicao;
import Up.Power.alimento.AlimentoId;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.refeicao.RefeicaoRepository;
import Up.Power.refeicao.TipoRefeicao;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        // Se o ID for 0 ou null, definir como null para que JPA faça INSERT (persist) em vez de UPDATE (merge)
        this.id = (id != null && id != 0) ? id : null;
        this.tipo = tipo;
        this.alimentosIds = alimentosIds != null ? alimentosIds : new java.util.ArrayList<>();
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
    @Transactional
    public Refeicao salvar(Refeicao refeicao) {
        System.out.println("[REFEICAO_REPOSITORY] ========================================");
        System.out.println("[REFEICAO_REPOSITORY] salvar() chamado");
        System.out.println("[REFEICAO_REPOSITORY] Refeicao ID: " + 
                (refeicao.getId() != null ? refeicao.getId().getId() : "null"));
        
        RefeicaoJpa entity = mapper.toEntity(refeicao);
        System.out.println("[REFEICAO_REPOSITORY] Entity mapeada. ID antes de salvar: " + entity.getId());
        System.out.println("[REFEICAO_REPOSITORY] Tipo: " + entity.getTipo());
        System.out.println("[REFEICAO_REPOSITORY] Alimentos IDs: " + 
                (entity.getAlimentosIds() != null ? entity.getAlimentosIds() : "null"));
        System.out.println("[REFEICAO_REPOSITORY] Calorias totais: " + entity.getCaloriasTotais());
        
        // Usar jpaRepository.save() que automaticamente faz persist() ou merge() baseado no ID
        // Se a entidade tem ID null, será um INSERT (persist)
        // Se a entidade tem ID não-null, será um UPDATE (merge)
        System.out.println("[REFEICAO_REPOSITORY] Chamando repo.save()...");
        RefeicaoJpa salvo = repo.save(entity);
        System.out.println("[REFEICAO_REPOSITORY] repo.save() concluído com sucesso!");
        System.out.println("[REFEICAO_REPOSITORY] ID gerado pelo banco: " + salvo.getId());
        
        Refeicao domain = mapper.toDomain(salvo);
        
        System.out.println("[REFEICAO_REPOSITORY] Domain convertido. ID: " + 
                (domain.getId() != null ? domain.getId().getId() : "null"));
        System.out.println("[REFEICAO_REPOSITORY] ========================================");
        return domain;
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


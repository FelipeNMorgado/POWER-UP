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
    @Column(name = "categoria", nullable = false, length = 50)
    private Categoria categoria;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer calorias;

    @Column(name = "gramas", nullable = false)
    private Float gramas;

    public AlimentoJpa() {}

    public AlimentoJpa(Integer id, Categoria categoria, String nome, Integer calorias, Float gramas) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.calorias = calorias;
        this.gramas = gramas;
    }

    // Getters
    public Integer getId() { return id; }
    public Categoria getCategoria() { return categoria; }
    public String getNome() { return nome; }
    public Integer getCalorias() { return calorias; }
    public Float getGramas() { return gramas; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCalorias(Integer calorias) { this.calorias = calorias; }
    public void setGramas(Float gramas) { this.gramas = gramas; }
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

    @Override
    public java.util.List<Alimento> listarTodos() {
        System.out.println("[ALIMENTO_REPOSITORY] ========================================");
        System.out.println("[ALIMENTO_REPOSITORY] listarTodos() chamado");
        try {
            java.util.List<AlimentoJpa> entities = repo.findAll();
            System.out.println("[ALIMENTO_REPOSITORY] Total de entidades JPA encontradas: " + entities.size());
            
            if (entities.isEmpty()) {
                System.out.println("[ALIMENTO_REPOSITORY] Nenhuma entidade encontrada");
                System.out.println("[ALIMENTO_REPOSITORY] ========================================");
                return java.util.Collections.emptyList();
            }
            
            // Log da primeira entidade para debug
            if (!entities.isEmpty()) {
                AlimentoJpa first = entities.get(0);
                System.out.println("[ALIMENTO_REPOSITORY] Primeira entidade: id=" + first.getId() + 
                        ", nome=" + first.getNome() + 
                        ", categoria=" + (first.getCategoria() != null ? first.getCategoria().name() : "null") +
                        ", calorias=" + first.getCalorias() +
                        ", gramas=" + first.getGramas());
            }
            
            java.util.List<Alimento> result = entities.stream()
                    .map(mapper::toDomain)
                    .collect(java.util.stream.Collectors.toList());
            
            System.out.println("[ALIMENTO_REPOSITORY] Total de domains convertidos: " + result.size());
            System.out.println("[ALIMENTO_REPOSITORY] ========================================");
            return result;
        } catch (Exception e) {
            System.err.println("[ALIMENTO_REPOSITORY] ERRO ao listar todos:");
            System.err.println("[ALIMENTO_REPOSITORY] Tipo: " + e.getClass().getName());
            System.err.println("[ALIMENTO_REPOSITORY] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[ALIMENTO_REPOSITORY] Causa: " + (e.getCause() != null ? e.getCause().getClass().getName() + " - " + e.getCause().getMessage() : "null"));
            e.printStackTrace();
            System.err.println("[ALIMENTO_REPOSITORY] ========================================");
            throw e;
        }
    }
}


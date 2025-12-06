package Up.Power.infraestrutura.persistencia.jpa.planoTreino;

import Up.Power.Email;
import Up.Power.EstadoPlano;
import Up.Power.PlanoTreino;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.treino.TipoTreino;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// =====================================
// =========== EMBEDDABLE ==============
// =====================================

@Embeddable
class TreinoEmbedded {
    @Column(name = "treino_id")
    private Integer treinoId;
    
    @Column(name = "exercicio_id")
    private Integer exercicioId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_treino")
    private TipoTreino tipo;
    
    @Column(name = "repeticoes")
    private Integer repeticoes;
    
    @Column(name = "peso")
    private Float peso;
    
    @Column(name = "series")
    private Integer series;
    
    @Column(name = "recorde_carga")
    private Float recordeCarga;

    public TreinoEmbedded() {}

    public TreinoEmbedded(Integer treinoId, Integer exercicioId, TipoTreino tipo,
                         Integer repeticoes, Float peso, Integer series, Float recordeCarga) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.tipo = tipo;
        this.repeticoes = repeticoes;
        this.peso = peso;
        this.series = series;
        this.recordeCarga = recordeCarga;
    }

    // Getters
    public Integer getTreinoId() { return treinoId; }
    public Integer getExercicioId() { return exercicioId; }
    public TipoTreino getTipo() { return tipo; }
    public Integer getRepeticoes() { return repeticoes; }
    public Float getPeso() { return peso; }
    public Integer getSeries() { return series; }
    public Float getRecordeCarga() { return recordeCarga; }

    // Setters
    public void setTreinoId(Integer treinoId) { this.treinoId = treinoId; }
    public void setExercicioId(Integer exercicioId) { this.exercicioId = exercicioId; }
    public void setTipo(TipoTreino tipo) { this.tipo = tipo; }
    public void setRepeticoes(Integer repeticoes) { this.repeticoes = repeticoes; }
    public void setPeso(Float peso) { this.peso = peso; }
    public void setSeries(Integer series) { this.series = series; }
    public void setRecordeCarga(Float recordeCarga) { this.recordeCarga = recordeCarga; }
}

// =====================================
// =========== ENTITY ==================
// =====================================

@Entity
@Table(name = "plano_treino")
public class PlanoTreinoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPlano estado;

    @ElementCollection
    @CollectionTable(
            name = "plano_treino_dias",
            joinColumns = @JoinColumn(name = "plano_id")
    )
    @Column(name = "dia")
    @Enumerated(EnumType.STRING)
    private List<Dias> dias;

    @ElementCollection
    @CollectionTable(
            name = "plano_treino_treinos",
            joinColumns = @JoinColumn(name = "plano_id")
    )
    private List<TreinoEmbedded> treinos;

    public PlanoTreinoJpa() {}

    public PlanoTreinoJpa(Integer id, String usuarioEmail, String nome,
                          EstadoPlano estado, List<Dias> dias,
                          List<TreinoEmbedded> treinos) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.nome = nome;
        this.estado = estado;
        this.dias = dias;
        this.treinos = treinos;
    }

    // Getters
    public Integer getId() { return id; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getNome() { return nome; }
    public EstadoPlano getEstado() { return estado; }
    public List<Dias> getDias() { return dias; }
    public List<TreinoEmbedded> getTreinos() { return treinos; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEstado(EstadoPlano estado) { this.estado = estado; }
    public void setDias(List<Dias> dias) { this.dias = dias; }
    public void setTreinos(List<TreinoEmbedded> treinos) { this.treinos = treinos; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaPlanoTreinoRepository extends JpaRepository<PlanoTreinoJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class PlanoTreinoRepositoryImpl implements PlanoTreinoRepository {

    private final JpaPlanoTreinoRepository repo;
    private final PlanoTreinoMapper mapper;

    public PlanoTreinoRepositoryImpl(JpaPlanoTreinoRepository repo, PlanoTreinoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(PlanoTreino plano) {
        PlanoTreinoJpa entity = mapper.toEntity(plano);
        repo.save(entity);
    }

    @Override
    public void excluir(PlanoTId id) {
        repo.deleteById(id.getId());
    }

    @Override
    public List<PlanoTreino> listar(PlanoTId id) {
        if (id != null) {
        return repo.findById(id.getId())
                .stream()
                    .map(mapper::toDomain)
                    .toList();
        }
        
        // Se não há filtro por ID, retorna todos os planos
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}

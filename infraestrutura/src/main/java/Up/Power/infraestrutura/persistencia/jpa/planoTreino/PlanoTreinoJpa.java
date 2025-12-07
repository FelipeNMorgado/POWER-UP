package Up.Power.infraestrutura.persistencia.jpa.planoTreino;

import Up.Power.Email;
import Up.Power.EstadoPlano;
import Up.Power.PlanoTreino;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.infraestrutura.persistencia.jpa.treino.TreinoJpa;
import Up.Power.infraestrutura.persistencia.jpa.treino.TreinoMapper;
import Up.Power.infraestrutura.persistencia.jpa.treino.JpaTreinoRepository;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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

    @ManyToMany
    @JoinTable(
            name = "plano_treino_treinos",
            joinColumns = @JoinColumn(name = "plano_id"),
            inverseJoinColumns = @JoinColumn(name = "treino_id")
    )
    private List<TreinoJpa> treinos;

    public PlanoTreinoJpa() {}

    public PlanoTreinoJpa(Integer id, String usuarioEmail, String nome,
                          EstadoPlano estado, List<Dias> dias,
                          List<TreinoJpa> treinos) {
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
    public List<TreinoJpa> getTreinos() { return treinos; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEstado(EstadoPlano estado) { this.estado = estado; }
    public void setDias(List<Dias> dias) { this.dias = dias; }
    public void setTreinos(List<TreinoJpa> treinos) { this.treinos = treinos; }
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
    private final JpaTreinoRepository treinoRepo;
    private final PlanoTreinoMapper mapper;

    public PlanoTreinoRepositoryImpl(JpaPlanoTreinoRepository repo, 
                                     JpaTreinoRepository treinoRepo,
                                     PlanoTreinoMapper mapper) {
        this.repo = repo;
        this.treinoRepo = treinoRepo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(PlanoTreino plano) {
        // Primeiro, salvar todos os treinos na tabela treino
        List<TreinoJpa> treinosJpa = plano.getTreinos().stream()
                .map(treino -> {
                    TreinoJpa treinoJpa = TreinoMapper.toEntity(treino);
                    return treinoRepo.save(treinoJpa); // Salva e retorna com ID gerado
                })
                .collect(Collectors.toList());
        
        // Depois, criar o plano com os treinos já salvos
        PlanoTreinoJpa entity = mapper.toEntity(plano, treinosJpa);
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

package Up.Power.infraestrutura.persistencia.jpa.planoTreino;

import Up.Power.Email;
import Up.Power.EstadoPlano;
import Up.Power.PlanoTreino;
import Up.Power.Treino;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.treino.TreinoId;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// =====================================
// =========== ENTITY ==================
// =====================================

@Entity
@Table(name = "plano_treino")
public class PlanoTreinoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String usuarioEmail;

    private String nome;

    @Enumerated(EnumType.STRING)
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
    @Column(name = "treino_id")
    private List<Integer> treinos; // apenas IDs dos treinos

    public PlanoTreinoJpa() {}

    public PlanoTreinoJpa(Integer id, String usuarioEmail, String nome,
                          EstadoPlano estado, List<Dias> dias,
                          List<Integer> treinos) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.nome = nome;
        this.estado = estado;
        this.dias = dias;
        this.treinos = treinos;
    }

    public Integer getId() { return id; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getNome() { return nome; }
    public EstadoPlano getEstado() { return estado; }
    public List<Dias> getDias() { return dias; }
    public List<Integer> getTreinos() { return treinos; }

    public void setId(Integer id) { this.id = id; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEstado(EstadoPlano estado) { this.estado = estado; }
    public void setDias(List<Dias> dias) { this.dias = dias; }
    public void setTreinos(List<Integer> treinos) { this.treinos = treinos; }
}



// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaPlanoTreinoRepository extends JpaRepository<PlanoTreinoJpa, Integer> {
}

@Repository
class PlanoTreinoRepositoryImpl implements PlanoTreinoRepository {

    private final JpaPlanoTreinoRepository repo;

    public PlanoTreinoRepositoryImpl(JpaPlanoTreinoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void salvar(PlanoTreino plano) {
        PlanoTreinoJpa entity = PlanoTreinoMapper.toEntity(plano);
        repo.save(entity);
    }

    @Override
    public void excluir(PlanoTId id) {
        repo.deleteById(id.getId());
    }

    @Override
    public List<PlanoTreino> listar(PlanoTId id) {
        return repo.findById(id.getId())
                .stream()
                .map(PlanoTreinoMapper::toDomain)
                .toList();
    }
}


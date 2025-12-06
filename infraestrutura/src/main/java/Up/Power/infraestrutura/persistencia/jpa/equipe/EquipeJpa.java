package Up.Power.infraestrutura.persistencia.jpa.equipe;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// =====================================
// =========== ENTITY ==================
// =====================================

@Entity
@Table(name = "equipe")
public class EquipeJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column
    private String foto;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "fim")
    private LocalDate fim;

    @Column(name = "usuario_adm", nullable = false)
    private String usuarioAdm;

    @ElementCollection
    @CollectionTable(
            name = "equipe_membros",
            joinColumns = @JoinColumn(name = "equipe_id")
    )
    @Column(name = "usuario_email")
    private List<String> usuariosEmails;

    public EquipeJpa() {}

    public EquipeJpa(Integer id, String nome, String descricao, String foto,
                     LocalDate inicio, LocalDate fim, String usuarioAdm,
                     List<String> usuariosEmails) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.foto = foto;
        this.inicio = inicio;
        this.fim = fim;
        this.usuarioAdm = usuarioAdm;
        this.usuariosEmails = usuariosEmails;
    }

    // Getters
    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getFoto() { return foto; }
    public LocalDate getInicio() { return inicio; }
    public LocalDate getFim() { return fim; }
    public String getUsuarioAdm() { return usuarioAdm; }
    public List<String> getUsuariosEmails() { return usuariosEmails; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public void setFim(LocalDate fim) { this.fim = fim; }
    public void setUsuarioAdm(String usuarioAdm) { this.usuarioAdm = usuarioAdm; }
    public void setUsuariosEmails(List<String> usuariosEmails) { this.usuariosEmails = usuariosEmails; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaEquipeRepository extends JpaRepository<EquipeJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class EquipeRepositoryImpl implements Up.Power.equipe.EquipeRepository {

    private final JpaEquipeRepository jpaRepository;
    private final EquipeMapper mapper;

    public EquipeRepositoryImpl(JpaEquipeRepository jpaRepository, EquipeMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Up.Power.Equipe equipe) {
        EquipeJpa entity = mapper.toEntity(equipe);
        jpaRepository.save(entity);
    }

    @Override
    public java.util.List<Up.Power.Equipe> listarEquipe(Up.Power.equipe.EquipeId id, Up.Power.perfil.PerfilId perfil) {
        if (id != null) {
            return jpaRepository.findById(id.getId())
                    .map(mapper::toDomain)
                    .map(java.util.List::of)
                    .orElse(java.util.List.of());
        }
        
        // Se não há filtro por ID, retorna todas as equipes
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}


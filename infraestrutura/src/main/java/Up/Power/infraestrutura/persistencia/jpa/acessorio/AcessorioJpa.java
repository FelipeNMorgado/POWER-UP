package Up.Power.infraestrutura.persistencia.jpa.acessorio;

import Up.Power.Acessorio;
import Up.Power.acessorio.AcessorioId;
import Up.Power.acessorio.AcessorioRepository;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ===========================
// ======== ENTITY ===========
// ===========================

@Entity
@Table(name = "acessorio")
public class AcessorioJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "LONGTEXT")
    private String icone;

    @Column(nullable = false)
    private Integer preco;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "LONGTEXT")
    private String imagem;

    public AcessorioJpa() {}

    public AcessorioJpa(Integer id, String icone, Integer preco, String nome, String imagem) {
        this.id = id;
        this.icone = icone;
        this.preco = preco;
        this.nome = nome;
        this.imagem = imagem;
    }

    // Getters
    public Integer getId() { return id; }
    public String getIcone() { return icone; }
    public Integer getPreco() { return preco; }
    public String getNome() { return nome; }
    public String getImagem() { return imagem; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setIcone(String icone) { this.icone = icone; }
    public void setPreco(Integer preco) { this.preco = preco; }
    public void setNome(String nome) { this.nome = nome; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaAcessorioRepository extends JpaRepository<AcessorioJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================

@Repository
class AcessorioRepositoryImpl implements AcessorioRepository {

    private final JpaAcessorioRepository repo;
    private final AcessorioMapper mapper;

    public AcessorioRepositoryImpl(JpaAcessorioRepository repo, AcessorioMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void save(Acessorio acessorio) {
        AcessorioJpa entity = mapper.toEntity(acessorio);
        repo.save(entity);
    }

    @Override
    public Optional<Acessorio> findById(Acessorio acessorio) {
        // A interface do repositório recebe Acessorio, mas precisamos do ID
        if (acessorio == null || acessorio.getId() == null) {
            return Optional.empty();
        }
        return repo.findById(acessorio.getId().getId())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Acessorio> findById(AcessorioId id) {
        if (id == null) {
            return Optional.empty();
        }
        return repo.findById(id.getId())
                .map(mapper::toDomain);
    }

    @Override
    public List<Acessorio> findAll() {
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}


package Up.Power.infraestrutura.persistencia.jpa.perfil;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Up.Power.Email;
import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "perfil")
public class PerfilJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean estado;

    @Column(nullable = false)
    private LocalDateTime criacao;

    @Column
    private String foto;

    // Nota: As listas (planosTreinos, conquistas, metas, amigos) são relações complexas
    // que podem ser carregadas via queries específicas quando necessário
    // Por enquanto, vamos persistir apenas os campos principais

    public PerfilJpa() {}

    public PerfilJpa(Integer id, String usuarioEmail, String username,
                     Boolean estado, LocalDateTime criacao, String foto) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.username = username;
        this.estado = estado;
        this.criacao = criacao;
        this.foto = foto;
    }

    // Getters
    public Integer getId() { return id; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getUsername() { return username; }
    public Boolean getEstado() { return estado; }
    public LocalDateTime getCriacao() { return criacao; }
    public String getFoto() { return foto; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public void setUsername(String username) { this.username = username; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public void setFoto(String foto) { this.foto = foto; }
}

interface JpaPerfilRepository extends JpaRepository<PerfilJpa, Integer> {
    Optional<PerfilJpa> findById(Integer id);
}

@Repository
class PerfilRepositoryImpl implements PerfilRepository {

    private final JpaPerfilRepository repo;

    public PerfilRepositoryImpl(JpaPerfilRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Perfil> findById(PerfilId id) {
        return repo.findById(id.getId())
                .map(PerfilMapper::toDomain);
    }

    @Override
    public Perfil save(Perfil perfil) {
        PerfilJpa entity = PerfilMapper.toEntity(perfil);
        PerfilJpa saved = repo.save(entity);
        return PerfilMapper.toDomain(saved);
    }

    @Override
    public boolean existsAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        // Implementação simplificada: verifica se ambos os perfis existem
        // A lógica completa de amizade pode ser implementada via query específica
        Optional<Perfil> perfil1 = findById(perfilId1);
        Optional<Perfil> perfil2 = findById(perfilId2);
        
        if (perfil1.isEmpty() || perfil2.isEmpty()) {
            return false;
        }
        
        // Verifica se são amigos (verifica na lista de amigos de cada perfil)
        return perfil1.get().getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfil2.get().getUsuarioEmail()))
                || perfil2.get().getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfil1.get().getUsuarioEmail()));
    }
}


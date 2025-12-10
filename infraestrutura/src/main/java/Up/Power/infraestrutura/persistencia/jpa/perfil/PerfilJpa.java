package Up.Power.infraestrutura.persistencia.jpa.perfil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Up.Power.infraestrutura.persistencia.jpa.consquista.ConquistaJpa;
import Up.Power.infraestrutura.persistencia.jpa.meta.MetaJpa;
import Up.Power.infraestrutura.persistencia.jpa.planoTreino.PlanoTreinoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

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

    // Relacionamentos ManyToMany
    @ManyToMany
    @JoinTable(
            name = "perfil_plano_treino",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "plano_treino_id")
    )
    private List<PlanoTreinoJpa> planosTreinos;

    @ManyToMany
    @JoinTable(
            name = "perfil_conquista",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "conquista_id")
    )
    private List<ConquistaJpa> conquistas;

    @ManyToMany
    @JoinTable(
            name = "perfil_meta",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "meta_id")
    )
    private List<MetaJpa> metas;

    @ManyToMany
    @JoinTable(
            name = "perfil_amigos",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_perfil_id")
    )
    private List<PerfilJpa> amigos;

    public PerfilJpa() {
        // Inicializar listas para evitar NullPointerException
        this.planosTreinos = new ArrayList<>();
        this.conquistas = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.amigos = new ArrayList<>();
    }

    public PerfilJpa(Integer id, String usuarioEmail, String username,
                     Boolean estado, LocalDateTime criacao, String foto,
                     List<PlanoTreinoJpa> planosTreinos, List<ConquistaJpa> conquistas,
                     List<MetaJpa> metas, List<PerfilJpa> amigos) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.username = username;
        this.estado = estado;
        this.criacao = criacao;
        this.foto = foto;
        this.planosTreinos = planosTreinos;
        this.conquistas = conquistas;
        this.metas = metas;
        this.amigos = amigos;
    }

    // Getters
    public Integer getId() { return id; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getUsername() { return username; }
    public Boolean getEstado() { return estado; }
    public LocalDateTime getCriacao() { return criacao; }
    public String getFoto() { return foto; }
    public List<PlanoTreinoJpa> getPlanosTreinos() { return planosTreinos; }
    public List<ConquistaJpa> getConquistas() { return conquistas; }
    public List<MetaJpa> getMetas() { return metas; }
    public List<PerfilJpa> getAmigos() { return amigos; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public void setUsername(String username) { this.username = username; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setPlanosTreinos(List<PlanoTreinoJpa> planosTreinos) { this.planosTreinos = planosTreinos; }
    public void setConquistas(List<ConquistaJpa> conquistas) { this.conquistas = conquistas; }
    public void setMetas(List<MetaJpa> metas) { this.metas = metas; }
    public void setAmigos(List<PerfilJpa> amigos) { this.amigos = amigos; }
}

interface JpaPerfilRepository extends JpaRepository<PerfilJpa, Integer> {
        Optional<PerfilJpa> findById(Integer id);
        Optional<PerfilJpa> findByUsuarioEmail(String usuarioEmail);

        @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
                   "FROM PerfilJpa p JOIN p.amigos a " +
                   "WHERE p.id = :p1 AND a.id = :p2")
        boolean existsFriendship(@Param("p1") Integer p1, @Param("p2") Integer p2);
        
        boolean existsByUsuarioEmail(String usuarioEmail);
        
        @Query("SELECT a FROM PerfilJpa p JOIN p.amigos a WHERE p.id = :perfilId")
        List<PerfilJpa> findAmigosDiretosByPerfilId(@Param("perfilId") Integer perfilId);
}


package Up.Power.infraestrutura.persistencia.jpa.usuario;

import Up.Power.Usuario;
import Up.Power.Email;
import Up.Power.usuario.UsuarioRepository;
import org.springframework.stereotype.Repository;
import Up.Power.AmizadeId;
import org.springframework.stereotype.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.ZoneId;

@Entity
@Table(name = "usuario")
public class UsuarioJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String usuarioEmail;

    @Column(name = "amizade_id")
    private Integer amizadeId;

    private String nome;
    private String senha;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascimento")
    private Date dataNascimento;

    public UsuarioJpa() {}

    public UsuarioJpa(String usuarioEmail, Integer amizadeId,
                      String nome, String senha, Date dataNascimento) {
        this.id = null;
        this.usuarioEmail = usuarioEmail;
        this.amizadeId = amizadeId;
        this.nome = nome;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsuarioEmail() { return usuarioEmail; }
    public Integer getAmizadeId() { return amizadeId; }
    public String getNome() { return nome; }
    public String getSenha() { return senha; }
    public Date getDataNascimento() { return dataNascimento; }

    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public void setAmizadeId(Integer amizadeId) { this.amizadeId = amizadeId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
}

@Repository
interface JpaUsuarioRepository extends JpaRepository<UsuarioJpa, Integer> {

    Optional<UsuarioJpa> findByUsuarioEmail(String email);
}

@Repository
class UsuarioRepositoryImpl implements UsuarioRepository {

    private final JpaUsuarioRepository jpaRepository;
    private final UsuarioMapper mapper;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaRepository, UsuarioMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario obter(Email usuarioEmail) {
        return jpaRepository.findByUsuarioEmail(usuarioEmail.getCaracteres())
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void salvar(Usuario usuario) {
        UsuarioJpa entity = mapper.toEntity(usuario);
        // preserve id if entity already exists (by email)
        jpaRepository.findByUsuarioEmail(entity.getUsuarioEmail()).ifPresent(existing -> entity.setId(existing.getId()));
        jpaRepository.save(entity);
    }

    @Override
    public Usuario obterPorId(Integer id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public boolean existePorEmail(Email usuarioEmail) {
        return jpaRepository.findByUsuarioEmail(usuarioEmail.getCaracteres()).isPresent();
    }

    @Override
    public boolean validarSenha(Email usuarioEmail, String senha) {
        return jpaRepository.findByUsuarioEmail(usuarioEmail.getCaracteres())
                .map(e -> e.getSenha() != null && e.getSenha().equals(senha))
                .orElse(false);
    }

    @Override
    public void atualizar(Usuario usuario) {
        UsuarioJpa entity = mapper.toEntity(usuario);
        jpaRepository.findByUsuarioEmail(entity.getUsuarioEmail()).ifPresent(existing -> entity.setId(existing.getId()));
        jpaRepository.save(entity);
    }

    @Override
    public void deletarPorId(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public java.util.List<Usuario> listarTodos() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(java.util.stream.Collectors.toList());
    }
}
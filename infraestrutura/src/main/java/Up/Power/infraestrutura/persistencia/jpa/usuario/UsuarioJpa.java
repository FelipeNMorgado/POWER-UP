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
import org.springframework.data.jpa.repository.Query;
import java.util.Date;

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

    public UsuarioJpa(Integer id, String usuarioEmail, Integer amizadeId,
                      String nome, String senha, Date dataNascimento) {
        this.id = id;
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
    
    @Query("SELECT MAX(u.amizadeId) FROM UsuarioJpa u WHERE u.amizadeId IS NOT NULL")
    Optional<Integer> findMaxAmizadeId();
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
        Optional<UsuarioJpa> entityOpt = jpaRepository.findByUsuarioEmail(usuarioEmail.getCaracteres());
        if (entityOpt.isPresent()) {
            UsuarioJpa entity = entityOpt.get();
            System.out.println("[DEBUG obter] Usuário encontrado: " + usuarioEmail.getCaracteres() + 
                             " amizadeId no banco: " + entity.getAmizadeId());
            Usuario domain = mapper.toDomain(entity);
            System.out.println("[DEBUG obter] amizadeId no domain: " + 
                             (domain.getCodigoAmizade() != null ? domain.getCodigoAmizade().getCodigo() : "null"));
            return domain;
        }
        System.out.println("[DEBUG obter] Usuário não encontrado: " + usuarioEmail.getCaracteres());
        return null;
    }

    @Override
    public void salvar(Usuario usuario) {
        String email = usuario.getUsuarioEmail().getCaracteres();
        
        // Verificar se o usuário já existe no banco
        Optional<UsuarioJpa> existingEntityOpt = jpaRepository.findByUsuarioEmail(email);
        
        if (existingEntityOpt.isPresent()) {
            // Se existe, atualizar a entidade existente (preservando o ID)
            UsuarioJpa existingEntity = existingEntityOpt.get();
            Integer existingId = existingEntity.getId();
            
            // Log para debug
            System.out.println("[DEBUG salvar] Atualizando usuário existente: " + email + " com ID: " + existingId);
            System.out.println("[DEBUG salvar] amizadeId do domain: " + (usuario.getCodigoAmizade() != null ? usuario.getCodigoAmizade().getCodigo() : "null"));
            System.out.println("[DEBUG salvar] amizadeId atual no banco: " + existingEntity.getAmizadeId());
            
            // Atualizar os campos que podem ter mudado
            Integer novoAmizadeId = usuario.getCodigoAmizade() != null ? usuario.getCodigoAmizade().getCodigo() : null;
            existingEntity.setAmizadeId(novoAmizadeId);
            
            System.out.println("[DEBUG salvar] amizadeId após set: " + existingEntity.getAmizadeId());
            existingEntity.setNome(usuario.getNome());
            existingEntity.setSenha(usuario.getSenha());
            existingEntity.setDataNascimento(mapper.toDate(usuario.getDataNascimento()));
            
            // Garantir que o ID está presente
            if (existingId != null) {
                existingEntity.setId(existingId);
            }
            
            // Salvar a entidade existente (com ID preservado, fará UPDATE)
            jpaRepository.save(existingEntity);
        } else {
            // Se não existe, criar nova entidade
            System.out.println("[DEBUG] Criando novo usuário: " + email);
            UsuarioJpa newEntity = mapper.toEntity(usuario);
            jpaRepository.save(newEntity);
        }
    }
    
    @Override
    public int obterProximoAmizadeId() {
        // Buscar o maior amizadeId do banco
        Optional<Integer> maxId = jpaRepository.findMaxAmizadeId();
        // Se não houver nenhum, começar com 1, senão incrementar
        return maxId.map(id -> id + 1).orElse(1);
    }
}
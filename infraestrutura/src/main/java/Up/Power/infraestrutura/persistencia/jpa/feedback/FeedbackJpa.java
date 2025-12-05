package Up.Power.infraestrutura.persistencia.jpa.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.Classificacao;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackRepository;
import Up.Power.frequencia.FrequenciaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "feedback")
public class FeedbackJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "frequencia_id", nullable = false)
    private Integer frequencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Classificacao classificacao;

    @Column(name = "feedback_texto")
    private String feedback;

    @Column(name = "email_usuario")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    public FeedbackJpa() {}

    public FeedbackJpa(Integer id, Integer frequencia, Classificacao classificacao, String feedback, String email, Date data) {
        this.id = id;
        this.frequencia = frequencia;
        this.classificacao = classificacao;
        this.feedback = feedback;
        this.email = email;
        this.data = data;
    }

    public Integer getId() { return id; }
    public Integer getFrequencia() { return frequencia; }
    public Classificacao getClassificacao() { return classificacao; }
    public String getFeedback() { return feedback; }
    public String getEmail() { return email; }
    public Date getData() { return data; }
    public void setId(int id) {this.id = id;}
    public void setFrequencia(int id) {this.id = id;}
    public void setClassificacao(Classificacao classificacao) {this.classificacao = classificacao;}
    public void setFeedback(String feedback) {this.feedback = feedback;}
    public void setEmail(String caracteres) {this.email = email;}
    public void setData(Date data) {this.data = data;}
}


interface JpaFeedbackRepository extends JpaRepository<FeedbackJpa, Integer> {
    @Query("SELECT f FROM FeedbackJpa f WHERE f.data = :data")
    Optional<FeedbackJpa> findByData(Date data);

    @Query("SELECT f FROM FeedbackJpa f WHERE f.frequencia = :frequencia")
    Optional<FeedbackJpa> findByFrequencia(Integer frequencia);

    @Query("SELECT f FROM FeedbackJpa f WHERE f.email = :email")
    List<FeedbackJpa> findAllByUsuarioEmail(String email);
}

@Repository
class FeedbackRepositoryImpl implements FeedbackRepository {

    private final JpaFeedbackRepository jpaRepo;
    private final FeedbackMapper mapper;

    public FeedbackRepositoryImpl(JpaFeedbackRepository jpaRepo, FeedbackMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Feedback salvar(Feedback feedback) {
        FeedbackJpa entity = mapper.toEntity(feedback);
        FeedbackJpa saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deletar(FeedbackId id) {
        jpaRepo.deleteById(id.getId());
    }

    @Override
    public List<Feedback> listarFeedbacks(FeedbackId feedback, Email usuarioEmail) {
        return jpaRepo.findAllByUsuarioEmail(usuarioEmail.getCaracteres())
                .stream()
                .map(FeedbackMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Feedback obter(FeedbackId id) {
        return jpaRepo.findById(id.getId())
                .map(FeedbackMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Feedback obterPorData(Date data) {
        return jpaRepo.findByData(data)
                .map(FeedbackMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Feedback obterPorFrequencia(FrequenciaId frequenciaId) {
        return jpaRepo.findByFrequencia(frequenciaId.getId())
                .map(FeedbackMapper::toDomain)
                .orElse(null);
    }

    @Override
    public void alterar(FeedbackId id) {

    }
}
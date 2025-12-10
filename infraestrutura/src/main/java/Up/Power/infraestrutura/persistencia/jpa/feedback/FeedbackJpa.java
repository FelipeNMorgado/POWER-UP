package Up.Power.infraestrutura.persistencia.jpa.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.Classificacao;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackRepository;
import Up.Power.frequencia.FrequenciaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Column(name = "data")
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
    public void setId(Integer id) {this.id = id;}
    public void setFrequencia(Integer frequencia) {this.frequencia = frequencia;}
    public void setClassificacao(Classificacao classificacao) {this.classificacao = classificacao;}
    public void setFeedback(String feedback) {this.feedback = feedback;}
    public void setEmail(String email) {this.email = email;}
    public void setData(Date data) {this.data = data;}
}


interface JpaFeedbackRepository extends JpaRepository<FeedbackJpa, Integer> {
    @Query("SELECT f FROM FeedbackJpa f WHERE f.data = :data")
    Optional<FeedbackJpa> findByData(Date data);

    @Query("SELECT f FROM FeedbackJpa f WHERE f.frequencia = :frequencia")
    Optional<FeedbackJpa> findByFrequencia(@Param("frequencia") Integer frequencia);

    @Query("SELECT f FROM FeedbackJpa f WHERE f.email = :email")
    List<FeedbackJpa> findAllByUsuarioEmail(@Param("email") String email);
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
        System.out.println("[FEEDBACK_REPOSITORY] ========================================");
        System.out.println("[FEEDBACK_REPOSITORY] Salvando feedback");
        System.out.println("[FEEDBACK_REPOSITORY] ID: " + (feedback.getId() != null ? feedback.getId().getId() : "null"));
        System.out.println("[FEEDBACK_REPOSITORY] Frequencia: " + (feedback.getFrequencia() != null ? feedback.getFrequencia().getId() : "null"));
        System.out.println("[FEEDBACK_REPOSITORY] Classificacao: " + feedback.getClassificacao());
        System.out.println("[FEEDBACK_REPOSITORY] Email: " + (feedback.getEmail() != null ? feedback.getEmail().getCaracteres() : "null"));
        System.out.println("[FEEDBACK_REPOSITORY] Feedback texto: " + (feedback.getFeedback() != null ? feedback.getFeedback().substring(0, Math.min(50, feedback.getFeedback().length())) + "..." : "null"));
        
        try {
            FeedbackJpa entity = mapper.toEntity(feedback);
            System.out.println("[FEEDBACK_REPOSITORY] Entity criada. ID: " + entity.getId());
            FeedbackJpa saved = jpaRepo.save(entity);
            System.out.println("[FEEDBACK_REPOSITORY] Entity salva. ID gerado: " + saved.getId());
            Feedback domain = mapper.toDomain(saved);
            System.out.println("[FEEDBACK_REPOSITORY] Domain convertido. ID: " + (domain.getId() != null ? domain.getId().getId() : "null"));
            System.out.println("[FEEDBACK_REPOSITORY] ========================================");
            return domain;
        } catch (Exception e) {
            System.err.println("[FEEDBACK_REPOSITORY] ERRO ao salvar:");
            System.err.println("[FEEDBACK_REPOSITORY] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_REPOSITORY] Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deletar(FeedbackId id) {
        jpaRepo.deleteById(id.getId());
    }

    @Override
    public List<Feedback> listarFeedbacks(FeedbackId feedback, Email usuarioEmail) {
        System.out.println("[FEEDBACK_REPOSITORY] ========================================");
        System.out.println("[FEEDBACK_REPOSITORY] Listando feedbacks por usuário");
        System.out.println("[FEEDBACK_REPOSITORY] Email: " + (usuarioEmail != null ? usuarioEmail.getCaracteres() : "null"));
        try {
            String email = usuarioEmail != null ? usuarioEmail.getCaracteres() : null;
            if (email == null || email.isEmpty()) {
                System.err.println("[FEEDBACK_REPOSITORY] ERRO: Email é null ou vazio!");
                return new java.util.ArrayList<>();
            }
            
            System.out.println("[FEEDBACK_REPOSITORY] Buscando feedbacks no banco...");
            List<FeedbackJpa> jpaEntities = jpaRepo.findAllByUsuarioEmail(email);
            System.out.println("[FEEDBACK_REPOSITORY] Entidades JPA encontradas: " + jpaEntities.size());
            
            List<Feedback> feedbacks = jpaEntities.stream()
                    .map(entity -> {
                        try {
                            System.out.println("[FEEDBACK_REPOSITORY] Convertendo entidade ID=" + entity.getId());
                            return FeedbackMapper.toDomain(entity);
                        } catch (Exception e) {
                            System.err.println("[FEEDBACK_REPOSITORY] ERRO ao converter entidade ID=" + entity.getId() + ": " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(f -> f != null)
                    .collect(Collectors.toList());
            
            System.out.println("[FEEDBACK_REPOSITORY] Feedbacks convertidos: " + feedbacks.size());
            System.out.println("[FEEDBACK_REPOSITORY] ========================================");
            return feedbacks;
        } catch (Exception e) {
            System.err.println("[FEEDBACK_REPOSITORY] ========================================");
            System.err.println("[FEEDBACK_REPOSITORY] ERRO ao listar feedbacks:");
            System.err.println("[FEEDBACK_REPOSITORY] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_REPOSITORY] Mensagem: " + e.getMessage());
            System.err.println("[FEEDBACK_REPOSITORY] Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            e.printStackTrace();
            System.err.println("[FEEDBACK_REPOSITORY] ========================================");
            throw e;
        }
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
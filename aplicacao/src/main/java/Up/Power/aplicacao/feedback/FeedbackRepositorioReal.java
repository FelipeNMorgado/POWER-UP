package Up.Power.aplicacao.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FeedbackRepositorioReal implements FeedbackRepositorioAplicacao {

    private final FeedbackRepository repo;

    public FeedbackRepositorioReal(FeedbackRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<FeedbackResumo> listarPorUsuario(String email) {
        System.out.println("[FEEDBACK_REPO_REAL] Listando feedbacks para: " + email);
        try {
            List<Feedback> feedbacks = repo.listarFeedbacks(null, new Email(email));
            System.out.println("[FEEDBACK_REPO_REAL] Feedbacks do repositório: " + feedbacks.size());
            
            List<FeedbackResumo> resumos = feedbacks.stream()
                    .map(f -> {
                        try {
                            return new FeedbackResumo(
                                    f.getId().getId(),
                                    f.getFrequencia().getId(),
                                    f.getClassificacao(),
                                    f.getFeedback() != null ? f.getFeedback() : "",
                                    f.getEmail().getCaracteres(),
                                    f.getData(),
                                    null // Nome do plano será enriquecido no FeedbackServicoAplicacao
                            );
                        } catch (Exception e) {
                            System.err.println("[FEEDBACK_REPO_REAL] ERRO ao criar resumo: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(r -> r != null)
                    .collect(Collectors.toList());
            
            System.out.println("[FEEDBACK_REPO_REAL] Resumos criados: " + resumos.size());
            return resumos;
        } catch (Exception e) {
            System.err.println("[FEEDBACK_REPO_REAL] ERRO ao listar feedbacks:");
            System.err.println("[FEEDBACK_REPO_REAL] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_REPO_REAL] Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Feedback obter(Integer id) {
        var feedback = repo.obter(new FeedbackId(id)); // aqui chama JPA
        return feedback; // retorna a entidade
    }

    @Override
    public Feedback criar(Feedback feedback) {
        return repo.salvar(feedback);
    }

    @Override
    public Feedback modificar(Feedback feedback) {
        return repo.salvar(feedback);
    }

    @Override
    public void excluir(Integer id) {
        repo.deletar(new FeedbackId(id));
    }
}


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
        return repo.listarFeedbacks(null, new Email(email))
                .stream()
                .map(f -> new FeedbackResumo(
                        f.getId().getId(),
                        f.getFrequencia().getId(),
                        f.getClassificacao(),
                        f.getFeedback(),
                        f.getEmail().getCaracteres(),
                        f.getData()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Feedback obter(Integer id) {
        var feedback = repo.obter(new FeedbackId(id)); // aqui chama JPA
        return feedback; // retorna a entidade
    }

}


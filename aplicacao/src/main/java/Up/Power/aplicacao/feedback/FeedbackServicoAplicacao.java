package Up.Power.aplicacao.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackRepository;
import Up.Power.feedback.FeedbackId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServicoAplicacao {

    private final FeedbackRepository repo;

    public FeedbackServicoAplicacao(FeedbackRepository repo) {
        this.repo = repo;
    }

    public FeedbackResumo obter(Integer id) {
        var feedback = repo.obter(new FeedbackId(id));
        return feedback == null ? null : toResumo(feedback);
    }

    public List<FeedbackResumo> listarPorUsuario(String email) {
        return repo.listarFeedbacks(null, new Email(email))
                .stream()
                .map(this::toResumo)
                .collect(Collectors.toList());
    }

    private FeedbackResumo toResumo(Feedback f) {
        return new FeedbackResumo(
                f.getId().getId(),
                f.getFrequencia().getId(),
                f.getClassificacao(),
                f.getFeedback(),
                f.getEmail().getCaracteres(),
                f.getData()
        );
    }
}

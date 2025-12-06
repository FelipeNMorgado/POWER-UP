package Up.Power.aplicacao.feedback;

import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServicoAplicacao {

    private final FeedbackRepositorioAplicacao repo;

    public FeedbackServicoAplicacao(FeedbackRepositorioAplicacao repo) {
        this.repo = repo;
    }

    public FeedbackResumo obter(Integer id) {
        var feedback = repo.obter(new FeedbackId(id).getId());
        return feedback == null ? null : toResumo(feedback);
    }

    public List<FeedbackResumo> listarPorUsuario(String email) {
        return repo.listarPorUsuario(email);
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

package Up.Power.feedback;

import Up.Power.Email;
import Up.Power.Feedback;

import java.util.List;

public interface FeedbackRepository {
    void salvar(Feedback feedback);
    void deletar(FeedbackId id);
    List<Feedback> listarFeedbacks(FeedbackId feedback, Email usuarioEmail);
    Feedback obter(FeedbackId id);
    void alterar(FeedbackId id);
}



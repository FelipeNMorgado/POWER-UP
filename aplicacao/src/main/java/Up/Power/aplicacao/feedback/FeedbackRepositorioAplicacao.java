package Up.Power.aplicacao.feedback;

import Up.Power.Feedback;

import java.util.List;

public interface FeedbackRepositorioAplicacao {
    List<FeedbackResumo> listarPorUsuario(String email);
    Feedback obter(Integer id);
}

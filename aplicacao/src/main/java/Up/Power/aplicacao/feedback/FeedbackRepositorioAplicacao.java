package Up.Power.aplicacao.feedback;

import java.util.List;

public interface FeedbackRepositorioAplicacao {
    List<FeedbackResumo> listarPorUsuario(String email);
}

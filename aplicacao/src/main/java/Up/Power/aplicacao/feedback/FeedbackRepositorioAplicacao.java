package Up.Power.aplicacao.feedback;

import Up.Power.Feedback;

import java.util.List;

public interface FeedbackRepositorioAplicacao {
    List<FeedbackResumo> listarPorUsuario(String email);
    Feedback obter(Integer id);
    Feedback criar(Feedback feedback);
    Feedback modificar(Feedback feedback);
    void excluir(Integer id);
}

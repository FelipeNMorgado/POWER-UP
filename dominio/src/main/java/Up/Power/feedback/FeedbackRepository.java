package Up.Power.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.frequencia.FrequenciaId;

import java.util.Date;
import java.util.List;

public interface FeedbackRepository {
    void salvar(Feedback feedback);
    void deletar(FeedbackId id);
    List<Feedback> listarFeedbacks(FeedbackId feedback, Email usuarioEmail);
    Feedback obter(FeedbackId id);
    Feedback obterPorData(Date data);
    Feedback obterPorFrequencia(FrequenciaId frequenciaId);
    void alterar(FeedbackId id);
}



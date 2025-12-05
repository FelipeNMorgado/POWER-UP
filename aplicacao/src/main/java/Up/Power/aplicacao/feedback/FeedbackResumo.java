package Up.Power.aplicacao.feedback;

import Up.Power.feedback.Classificacao;
import java.util.Date;

public record FeedbackResumo(
        Integer id,
        Integer frequencia,
        Classificacao classificacao,
        String feedback,
        String email,
        Date data
) {}

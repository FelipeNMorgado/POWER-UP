package Up.Power.aplicacao.frequencia;

import java.time.LocalDateTime;

public record FrequenciaResumo(
        Integer id,
        Integer perfilId,
        Integer treinoId,
        LocalDateTime dataDePresenca,
        Integer planoTreinoId,
        String foto
) {}

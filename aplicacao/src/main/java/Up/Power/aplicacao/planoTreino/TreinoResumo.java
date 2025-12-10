package Up.Power.aplicacao.planoTreino;

import Up.Power.treino.TipoTreino;
import java.time.LocalDateTime;

public record TreinoResumo(
        Integer id,
        Integer exercicioId,
        TipoTreino tipo,
        Integer repeticoes,
        Float peso,
        Integer series,
        Float recordeCarga,
        Float distancia,
        LocalDateTime tempo
) {}


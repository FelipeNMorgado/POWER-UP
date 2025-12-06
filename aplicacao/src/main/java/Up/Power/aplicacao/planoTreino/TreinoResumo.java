package Up.Power.aplicacao.planoTreino;

import Up.Power.treino.TipoTreino;

public record TreinoResumo(
        Integer id,
        Integer exercicioId,
        TipoTreino tipo,
        Integer repeticoes,
        Float peso,
        Integer series,
        Float recordeCarga
) {}


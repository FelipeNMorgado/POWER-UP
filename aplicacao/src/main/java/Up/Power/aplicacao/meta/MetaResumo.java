package Up.Power.aplicacao.meta;

import java.util.Date;

public record MetaResumo(
        Integer id,
        Integer exercicioId,
        Integer treinoId,
        String nome,
        Date dataFim,
        Date dataInicio
) {}
package Up.Power.aplicacao.meta;

import java.util.Date;

public record CriarMetaRequest(
        String nome,
        Integer exercicioId,
        Integer treinoId,
        Date dataInicio,
        Date dataFim,
        Double exigenciaMinima
) {}


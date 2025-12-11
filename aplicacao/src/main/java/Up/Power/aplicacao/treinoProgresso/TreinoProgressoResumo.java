package Up.Power.aplicacao.treinoProgresso;

import java.util.Date;

public record TreinoProgressoResumo(
        Integer id,
        Integer perfilId,
        Integer exercicioId,
        Date dataRegistro,
        Double pesoKg,
        Integer repeticoes,
        Integer series,
        Date createdAt
) {}


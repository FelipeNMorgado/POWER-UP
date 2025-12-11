package Up.Power.aplicacao.treinoProgresso;

public record RegistrarTreinoProgressoCommand(
        Integer perfilId,
        Integer exercicioId,
        String dataRegistro,
        Double pesoKg,
        Integer repeticoes,
        Integer series
) {}


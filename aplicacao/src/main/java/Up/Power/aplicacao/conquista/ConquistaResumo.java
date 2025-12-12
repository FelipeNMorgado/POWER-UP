package Up.Power.aplicacao.conquista;

public record ConquistaResumo(
        Integer id,
        Integer exercicioId,
        Integer treinoId,
        String nome,
        String descricao,
        boolean concluida,
        String badgeAtual,
        Float pesoMinimo,
        Integer atributoMinimo,
        String tipoAtributo,
        Integer repeticoesMinimas,
        Integer seriesMinimas
) {}
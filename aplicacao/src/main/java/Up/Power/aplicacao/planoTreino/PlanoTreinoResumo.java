package Up.Power.aplicacao.planoTreino;

import Up.Power.EstadoPlano;
import Up.Power.planoTreino.Dias;
import java.util.List;

public record PlanoTreinoResumo(
        Integer id,
        String usuarioEmail,
        String nome,
        EstadoPlano estado,
        List<Dias> dias,
        List<TreinoResumo> treinos
) {}


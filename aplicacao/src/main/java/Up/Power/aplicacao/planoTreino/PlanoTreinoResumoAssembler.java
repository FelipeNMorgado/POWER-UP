package Up.Power.aplicacao.planoTreino;

import Up.Power.PlanoTreino;
import Up.Power.Treino;

import java.util.List;
import java.util.stream.Collectors;

public final class PlanoTreinoResumoAssembler {

    private PlanoTreinoResumoAssembler() {}

    public static PlanoTreinoResumo toResumo(PlanoTreino plano) {
        if (plano == null) return null;

        List<TreinoResumo> treinos = plano.getTreinos().stream()
                .map(PlanoTreinoResumoAssembler::toTreinoResumo)
                .collect(Collectors.toList());

        return new PlanoTreinoResumo(
                plano.getId() != null ? plano.getId().getId() : null,
                plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : null,
                plano.getNome(),
                plano.getEstadoPlano(),
                plano.getDias(),
                treinos
        );
    }

    private static TreinoResumo toTreinoResumo(Treino treino) {
        return new TreinoResumo(
                treino.getId() != null ? treino.getId().getId() : null,
                treino.getExercicio() != null ? treino.getExercicio().getId() : null,
                treino.getTipo(),
                treino.getRepeticoes(),
                treino.getPeso(),
                treino.getSeries(),
                treino.getRecordeCarga(),
                treino.getDistancia(),
                treino.getTempo()
        );
    }
}


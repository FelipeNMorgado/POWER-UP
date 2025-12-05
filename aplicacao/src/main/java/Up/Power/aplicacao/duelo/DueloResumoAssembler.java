package Up.Power.aplicacao.duelo;

import Up.Power.Duelo;

public final class DueloResumoAssembler {

    private DueloResumoAssembler() {
    }

    public static DueloResumo toResumo(Duelo duelo) {
        return new DueloResumo(
                duelo.getId() != null ? duelo.getId().getId() : null,
                duelo.getAvatar1().getId(),
                duelo.getAvatar2().getId(),
                duelo.getResultado(),
                duelo.getDataDuelo()
        );
    }
}

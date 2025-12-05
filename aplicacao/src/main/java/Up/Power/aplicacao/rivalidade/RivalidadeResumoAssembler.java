package Up.Power.aplicacao.rivalidade;

import Up.Power.Rivalidade;
import Up.Power.infraestrutura.persistencia.jpa.rivalidade.RivalidadeJpa;

public final class RivalidadeResumoAssembler {

    private RivalidadeResumoAssembler() {}

    public static RivalidadeResumo toResumo(Rivalidade r) {
        if (r == null) return null;
        return new RivalidadeResumo(
                r.getId() != null ? r.getId().getId() : null,
                r.getPerfil1() != null ? r.getPerfil1().getId() : null,
                r.getPerfil2() != null ? r.getPerfil2().getId() : null,
                r.getDataConvite(),
                r.getInicio(),
                r.getFim(),
                r.getStatus()
        );
    }

    public static RivalidadeResumo toResumoFromEntity(RivalidadeJpa e) {
        if (e == null) return null;
        return new RivalidadeResumo(
                e.getId(),
                e.getPerfil1Id(),
                e.getPerfil2Id(),
                e.getDataConvite(),
                e.getInicio(),
                e.getFim(),
                e.getStatus()
        );
    }
}

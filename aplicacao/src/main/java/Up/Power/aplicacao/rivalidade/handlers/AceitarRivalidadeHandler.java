package Up.Power.aplicacao.rivalidade.handlers;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.aplicacao.rivalidade.commands.AceitarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AceitarRivalidadeHandler {

    private final RivalidadeService dominioService;

    public AceitarRivalidadeHandler(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }

    @Transactional
    public RivalidadeResumo handle(AceitarRivalidadeCommand cmd) {
        Rivalidade r = dominioService.aceitarConvite(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
        return RivalidadeResumoAssembler.toResumo(r);
    }
}

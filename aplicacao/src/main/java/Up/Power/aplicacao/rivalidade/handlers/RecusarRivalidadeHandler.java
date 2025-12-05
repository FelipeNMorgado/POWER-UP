package Up.Power.aplicacao.rivalidade.handlers;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.aplicacao.rivalidade.commands.RecusarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecusarRivalidadeHandler {

    private final RivalidadeService dominioService;

    public RecusarRivalidadeHandler(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }

    @Transactional
    public RivalidadeResumo handle(RecusarRivalidadeCommand cmd) {
        Rivalidade r = dominioService.recusarConvite(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
        return RivalidadeResumoAssembler.toResumo(r);
    }
}

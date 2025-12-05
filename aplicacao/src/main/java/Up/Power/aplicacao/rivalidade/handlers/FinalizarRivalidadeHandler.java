package Up.Power.aplicacao.rivalidade.handlers;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.aplicacao.rivalidade.commands.FinalizarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinalizarRivalidadeHandler {

    private final RivalidadeService dominioService;

    public FinalizarRivalidadeHandler(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }

    @Transactional
    public RivalidadeResumo handle(FinalizarRivalidadeCommand cmd) {
        Rivalidade r = dominioService.finalizarRivalidade(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
        return RivalidadeResumoAssembler.toResumo(r);
    }
}

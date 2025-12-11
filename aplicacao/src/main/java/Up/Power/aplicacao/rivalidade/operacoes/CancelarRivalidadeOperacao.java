package Up.Power.aplicacao.rivalidade.operacoes;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.commands.CancelarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Component;

@Component
public class CancelarRivalidadeOperacao extends OperacaoRivalidadeTemplate<CancelarRivalidadeCommand> {

    public CancelarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(CancelarRivalidadeCommand cmd) {
        return dominioService.cancelarConvite(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
    }
}


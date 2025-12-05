package Up.Power.aplicacao.rivalidade.operacoes;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.commands.AceitarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Component;

@Component
public class AceitarRivalidadeOperacao extends OperacaoRivalidadeTemplate<AceitarRivalidadeCommand> {

    public AceitarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(AceitarRivalidadeCommand cmd) {
        return dominioService.aceitarConvite(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
    }
}

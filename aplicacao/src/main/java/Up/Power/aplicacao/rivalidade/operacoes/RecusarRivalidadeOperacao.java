package Up.Power.aplicacao.rivalidade.operacoes;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.commands.RecusarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Component;

@Component
public class RecusarRivalidadeOperacao extends OperacaoRivalidadeTemplate<RecusarRivalidadeCommand> {

    public RecusarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(RecusarRivalidadeCommand cmd) {
        return dominioService.recusarConvite(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
    }
}

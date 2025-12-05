package Up.Power.aplicacao.rivalidade.operacoes;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.commands.FinalizarRivalidadeCommand;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Component;

@Component
public class FinalizarRivalidadeOperacao extends OperacaoRivalidadeTemplate<FinalizarRivalidadeCommand> {

    public FinalizarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(FinalizarRivalidadeCommand cmd) {
        return dominioService.finalizarRivalidade(
                new RivalidadeId(cmd.rivalidadeId()),
                new PerfilId(cmd.usuarioId())
        );
    }
}

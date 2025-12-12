package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class RecusarRivalidadeOperacao extends OperacaoRivalidadeTemplate {

    public RecusarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacaoRecusar(int rivalidadeId, int usuarioId) {
        return dominioService.recusarConvite(
                new RivalidadeId(rivalidadeId),
                new PerfilId(usuarioId)
        );
    }
}

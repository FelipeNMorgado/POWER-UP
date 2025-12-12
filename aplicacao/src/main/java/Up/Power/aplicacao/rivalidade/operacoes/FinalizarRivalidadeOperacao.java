package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class FinalizarRivalidadeOperacao extends OperacaoRivalidadeTemplate {

    public FinalizarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacaoFinalizar(int rivalidadeId, int usuarioId) {
        return dominioService.finalizarRivalidade(
                new RivalidadeId(rivalidadeId),
                new PerfilId(usuarioId)
        );
    }
}

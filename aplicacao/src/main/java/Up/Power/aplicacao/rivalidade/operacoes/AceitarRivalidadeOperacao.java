package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class AceitarRivalidadeOperacao extends OperacaoRivalidadeTemplate {

    public AceitarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacaoAceitar(int rivalidadeId, int usuarioId) {
        return dominioService.aceitarConvite(
                new RivalidadeId(rivalidadeId),
                new PerfilId(usuarioId)
        );
    }
}

package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class RecusarRivalidadeOperacao extends OperacaoRivalidadeTemplate<RecusarRivalidadeParametros> {

    public RecusarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(RecusarRivalidadeParametros params) {
        return dominioService.recusarConvite(
                new RivalidadeId(params.rivalidadeId()),
                new PerfilId(params.usuarioId())
        );
    }

    /**
     * Método de conveniência para manter compatibilidade com o código existente.
     */
    public RivalidadeResumo executarRecusar(int rivalidadeId, int usuarioId) {
        return executar(new RecusarRivalidadeParametros(rivalidadeId, usuarioId));
    }
}

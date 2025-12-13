package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class AceitarRivalidadeOperacao extends OperacaoRivalidadeTemplate<AceitarRivalidadeParametros> {

    public AceitarRivalidadeOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(AceitarRivalidadeParametros params) {
        return dominioService.aceitarConvite(
                new RivalidadeId(params.rivalidadeId()),
                new PerfilId(params.usuarioId())
        );
    }

    /**
     * Método de conveniência para manter compatibilidade com o código existente.
     */
    public RivalidadeResumo executarAceitar(int rivalidadeId, int usuarioId) {
        return executar(new AceitarRivalidadeParametros(rivalidadeId, usuarioId));
    }
}

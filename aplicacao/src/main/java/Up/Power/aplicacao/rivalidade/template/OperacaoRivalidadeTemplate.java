package Up.Power.aplicacao.rivalidade.template;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.transaction.annotation.Transactional;

public abstract class OperacaoRivalidadeTemplate<C> {

    protected final RivalidadeService dominioService;

    protected OperacaoRivalidadeTemplate(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }

    // Template Method — fluxo fixo
    @Transactional
    public final RivalidadeResumo executar(C command) {

        validar(command);

        Rivalidade rivalidade = executarOperacao(command);

        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }

    // Ganchos (steps customizáveis)
    protected void validar(C command) {}

    protected abstract Rivalidade executarOperacao(C command);
}

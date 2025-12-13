package Up.Power.aplicacao.rivalidade.template;

import org.springframework.transaction.annotation.Transactional;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.rivalidade.RivalidadeService;


public abstract class OperacaoRivalidadeTemplate<T> {
    
    protected final RivalidadeService dominioService;
    
    protected OperacaoRivalidadeTemplate(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }
    

    @Transactional
    public final RivalidadeResumo executar(T parametros) {
        validar(parametros);
        Rivalidade rivalidade = executarOperacao(parametros);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }
    

    protected void validar(T parametros) {
    }
    

    protected abstract Rivalidade executarOperacao(T parametros);
}

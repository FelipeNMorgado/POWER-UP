package Up.Power.aplicacao.rivalidade.template;

import org.springframework.transaction.annotation.Transactional;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.rivalidade.RivalidadeService;

/**
 * Template base genérico para operações de rivalidade.
 * Define o fluxo fixo: validar -> executar operação -> converter para resumo.
 * 
 * @param <T> Tipo dos parâmetros da operação
 */
public abstract class OperacaoRivalidadeTemplate<T> {
    
    protected final RivalidadeService dominioService;
    
    protected OperacaoRivalidadeTemplate(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }
    
    /**
     * Template Method - define o fluxo fixo da operação.
     * Este método é final para garantir que o algoritmo não seja alterado pelas subclasses.
     */
    @Transactional
    public final RivalidadeResumo executar(T parametros) {
        validar(parametros);
        Rivalidade rivalidade = executarOperacao(parametros);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }
    
    /**
     * Gancho opcional - pode ser sobrescrito pelas subclasses para adicionar validações específicas.
     * Implementação padrão vazia.
     */
    protected void validar(T parametros) {
        // Validação padrão vazia - subclasses podem sobrescrever
    }
    
    /**
     * Método abstrato - deve ser implementado pela subclasse para executar a operação específica.
     */
    protected abstract Rivalidade executarOperacao(T parametros);
}

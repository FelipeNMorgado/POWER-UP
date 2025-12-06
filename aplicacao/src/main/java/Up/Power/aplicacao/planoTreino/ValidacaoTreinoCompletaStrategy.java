package Up.Power.aplicacao.planoTreino;

import Up.Power.Treino;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Estratégia completa de validação: verifica se o treino tem todos os parâmetros necessários
 * (exercício, tipo, séries, repetições e carga).
 * Esta é a estratégia padrão (@Primary).
 */
@Primary
@Component
public class ValidacaoTreinoCompletaStrategy implements ValidacaoTreinoStrategy {

    @Override
    public boolean validar(Treino treino) {
        if (treino == null) {
            throw new IllegalArgumentException("Treino não pode ser nulo");
        }
        if (treino.getExercicio() == null) {
            throw new IllegalArgumentException("Treino deve ter um exercício definido");
        }
        if (treino.getTipo() == null) {
            throw new IllegalArgumentException("Treino deve ter um tipo definido");
        }
        if (treino.getSeries() <= 0) {
            throw new IllegalArgumentException("Treino deve ter pelo menos uma série");
        }
        if (treino.getRepeticoes() <= 0) {
            throw new IllegalArgumentException("Treino deve ter pelo menos uma repetição");
        }
        if (treino.getPeso() < 0) {
            throw new IllegalArgumentException("Carga do treino não pode ser negativa");
        }
        return true;
    }

    @Override
    public String getMensagemErro() {
        return "Treino inválido: deve ter exercício, tipo, séries, repetições e carga válidos";
    }
}


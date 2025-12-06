package Up.Power.aplicacao.planoTreino;

import Up.Power.Treino;
import org.springframework.stereotype.Component;

/**
 * Estratégia básica de validação: verifica se o treino tem exercício e tipo definidos.
 */
@Component
public class ValidacaoTreinoBasicaStrategy implements ValidacaoTreinoStrategy {

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
        return true;
    }

    @Override
    public String getMensagemErro() {
        return "Treino inválido: deve ter exercício e tipo definidos";
    }
}


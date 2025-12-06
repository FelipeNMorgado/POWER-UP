package Up.Power.aplicacao.planoTreino;

import Up.Power.Treino;

/**
 * Strategy pattern para diferentes estratégias de validação de treinos.
 * Permite intercambiar algoritmos de validação sem modificar o código cliente.
 */
public interface ValidacaoTreinoStrategy {
    
    /**
     * Valida um treino de acordo com a estratégia específica.
     * @param treino treino a ser validado
     * @return true se o treino é válido, false caso contrário
     * @throws IllegalArgumentException se o treino não atende aos critérios
     */
    boolean validar(Treino treino);
    
    /**
     * Retorna uma mensagem de erro caso a validação falhe.
     */
    String getMensagemErro();
}


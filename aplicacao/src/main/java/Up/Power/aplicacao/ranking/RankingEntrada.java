package Up.Power.aplicacao.ranking;

/**
 * DTO usado pelo frontend na tela de ranking.
 * Inclui dados básicos do perfil e o XP total calculado.
 */
public record RankingEntrada(
        Integer perfilId,
        String email,
        String username,
        String foto,
        Integer nivel,
        Integer xpTotal,
        Integer posicao,
        Integer equipeId
) {}


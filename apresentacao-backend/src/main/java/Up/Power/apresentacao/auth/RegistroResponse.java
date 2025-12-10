package Up.Power.apresentacao.auth;

public record RegistroResponse(
        boolean sucesso,
        String mensagem,
        String email,
        Integer perfilId,  // Pode ser null
        String username,
        Integer amizadeId
) {}


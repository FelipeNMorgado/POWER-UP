package Up.Power.apresentacao.auth;

public record LoginResponse(
        boolean sucesso,
        String mensagem,
        String email,
        Integer perfilId,
        String username
) {}


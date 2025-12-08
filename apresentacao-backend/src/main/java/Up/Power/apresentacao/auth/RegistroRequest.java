package Up.Power.apresentacao.auth;

import java.time.LocalDate;

public record RegistroRequest(
        String email,
        String nome,
        String senha,
        LocalDate dataNascimento,
        String username
) {}


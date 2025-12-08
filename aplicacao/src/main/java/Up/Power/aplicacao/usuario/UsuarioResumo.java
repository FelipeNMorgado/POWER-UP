package Up.Power.aplicacao.usuario;

import java.time.LocalDate;

public record UsuarioResumo(
        String usuarioEmail,
        Integer amizadeId,
        String nome,
        String senha,
        LocalDate dataNascimento
) {}

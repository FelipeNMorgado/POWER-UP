package Up.Power.aplicacao.equipe;

import java.time.LocalDate;
import java.util.List;

public record EquipeResumo(
        Integer id,
        String nome,
        String descricao,
        String foto,
        LocalDate inicio,
        LocalDate fim,
        String usuarioAdm,
        List<String> usuariosEmails,
        Integer quantidadeMembros
) {}


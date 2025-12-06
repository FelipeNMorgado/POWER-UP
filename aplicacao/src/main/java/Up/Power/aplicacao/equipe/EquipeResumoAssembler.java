package Up.Power.aplicacao.equipe;

import Up.Power.Equipe;
import Up.Power.Email;

import java.util.List;
import java.util.stream.Collectors;

public final class EquipeResumoAssembler {

    private EquipeResumoAssembler() {}

    public static EquipeResumo toResumo(Equipe equipe) {
        if (equipe == null) return null;
        
        List<String> emails = equipe.getUsuariosEmails().stream()
                .map(Email::getCaracteres)
                .collect(Collectors.toList());
        
        return new EquipeResumo(
                equipe.getId() != null ? equipe.getId().getId() : null,
                equipe.getNome(),
                equipe.getDescricao(),
                equipe.getFoto(),
                equipe.getInicio(),
                equipe.getFim(),
                equipe.getUsuarioAdm() != null ? equipe.getUsuarioAdm().getCaracteres() : null,
                emails,
                equipe.getQuantidadeMembros()
        );
    }
}


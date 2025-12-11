package Up.Power.aplicacao.avatar;

import java.util.List;

public record EquiparAcessoriosCommand(
        Integer avatarId,
        List<Integer> acessorioIds
) {}


package Up.Power.aplicacao.avatar;

import java.util.List;

public record AvatarResumo(
        Integer id,
        Integer perfilId,
        Integer nivel,
        Integer experiencia,
        Integer forca,
        Integer dinheiro,
        List<AcessorioResumo> acessorios
) {}
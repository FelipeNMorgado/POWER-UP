package Up.Power.aplicacao.rivalidade;

import Up.Power.rivalidade.StatusRivalidade;
import java.time.LocalDateTime;

public record RivalidadeResumo(
        Integer id,
        Integer perfil1,
        Integer perfil2,
        String nomePerfil1,
        String nomePerfil2,
        LocalDateTime dataConvite,
        LocalDateTime inicio,
        LocalDateTime fim,
        StatusRivalidade status
) {}

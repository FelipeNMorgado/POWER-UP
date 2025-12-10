package Up.Power.aplicacao.perfil;

import java.time.LocalDateTime;

public record PerfilResumo(
        Integer id,
        String usuarioEmail,
        String username,
        String foto,
        Boolean estado,
        LocalDateTime criacao
) {}


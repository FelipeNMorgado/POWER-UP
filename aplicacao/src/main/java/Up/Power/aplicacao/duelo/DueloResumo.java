package Up.Power.aplicacao.duelo;

import java.time.LocalDateTime;

public record DueloResumo(
        Integer id,
        Integer avatar1Id,
        Integer avatar2Id,
        String resultado,
        LocalDateTime dataDuelo
) {}

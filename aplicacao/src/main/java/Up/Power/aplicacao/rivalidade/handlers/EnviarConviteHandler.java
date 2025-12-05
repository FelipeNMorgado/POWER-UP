package Up.Power.aplicacao.rivalidade.handlers;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.aplicacao.rivalidade.commands.EnviarConviteCommand;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnviarConviteHandler {

    private final RivalidadeService dominioService;

    public EnviarConviteHandler(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }

    @Transactional
    public RivalidadeResumo handle(EnviarConviteCommand cmd) {
        Rivalidade r = dominioService.enviarConviteRivalidade(
                new PerfilId(cmd.perfil1Id()),
                new PerfilId(cmd.perfil2Id()),
                new ExercicioId(cmd.exercicioId())
        );
        return RivalidadeResumoAssembler.toResumo(r);
    }
}

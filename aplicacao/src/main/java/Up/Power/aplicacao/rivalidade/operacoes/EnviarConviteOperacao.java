package Up.Power.aplicacao.rivalidade.operacoes;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.commands.EnviarConviteCommand;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeService;
import org.springframework.stereotype.Component;

@Component
public class EnviarConviteOperacao extends OperacaoRivalidadeTemplate<EnviarConviteCommand> {

    public EnviarConviteOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(EnviarConviteCommand cmd) {
        return dominioService.enviarConviteRivalidade(
                new PerfilId(cmd.perfil1Id()),
                new PerfilId(cmd.perfil2Id()),
                new ExercicioId(cmd.exercicioId())
        );
    }
}

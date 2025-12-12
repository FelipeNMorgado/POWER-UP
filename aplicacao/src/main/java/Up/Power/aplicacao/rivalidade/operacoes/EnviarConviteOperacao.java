package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class EnviarConviteOperacao extends OperacaoRivalidadeTemplate {

    public EnviarConviteOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacaoEnviarConvite(int perfil1Id, int perfil2Id, int exercicioId) {
        return dominioService.enviarConviteRivalidade(
                new PerfilId(perfil1Id),
                new PerfilId(perfil2Id),
                new ExercicioId(exercicioId)
        );
    }
}

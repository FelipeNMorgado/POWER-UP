package Up.Power.aplicacao.rivalidade.operacoes;

import org.springframework.stereotype.Component;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.template.OperacaoRivalidadeTemplate;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeService;

@Component
public class EnviarConviteOperacao extends OperacaoRivalidadeTemplate<EnviarConviteParametros> {

    public EnviarConviteOperacao(RivalidadeService dominioService) {
        super(dominioService);
    }

    @Override
    protected Rivalidade executarOperacao(EnviarConviteParametros params) {
        return dominioService.enviarConviteRivalidade(
                new PerfilId(params.perfil1Id()),
                new PerfilId(params.perfil2Id()),
                new ExercicioId(params.exercicioId())
        );
    }

    /**
     * Método de conveniência para manter compatibilidade com o código existente.
     */
    public RivalidadeResumo executarEnviarConvite(int perfil1Id, int perfil2Id, int exercicioId) {
        return executar(new EnviarConviteParametros(perfil1Id, perfil2Id, exercicioId));
    }
}

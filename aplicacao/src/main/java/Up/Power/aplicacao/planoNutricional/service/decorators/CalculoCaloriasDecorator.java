package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.PlanoNutricional;
import Up.Power.Refeicao;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.refeicao.RefeicaoRepository;
// Removido @Component - será criado manualmente na configuração
public class CalculoCaloriasDecorator extends PlanoNutricionalDecorator {

    private final RefeicaoRepository refeicaoRepository;

    public CalculoCaloriasDecorator(
            PlanoNutricionalApplicationService next,
            RefeicaoRepository refeicaoRepository
    ) {
        super(next);
        this.refeicaoRepository = refeicaoRepository;
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.criar(command);
        aplicarCalculo(plano);
        return plano;
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.modificar(command);
        aplicarCalculo(plano);
        return plano;
    }

    private void aplicarCalculo(PlanoNutricional plano) {
        int total = plano.getRefeicoes().stream()
                .map(refeicaoRepository::obter)
                .mapToInt(Refeicao::getCaloriasTotais)
                .sum();

        plano.definirCaloriasTotais(total);
    }
}

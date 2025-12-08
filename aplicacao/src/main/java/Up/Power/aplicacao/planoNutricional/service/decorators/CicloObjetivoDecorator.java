package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.planoNutricional.Objetivo;
import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;
// Removido @Component - será criado manualmente na configuração
public class CicloObjetivoDecorator extends PlanoNutricionalDecorator {

    public CicloObjetivoDecorator(PlanoNutricionalApplicationService next) {
        super(next);
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.criar(command);
        ajustarMeta(plano);
        return plano;
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.modificar(command);
        ajustarMeta(plano);
        return plano;
    }

    private void ajustarMeta(PlanoNutricional plano) {

        int total = plano.getCaloriasTotais();
        int meta = switch (plano.getObjetivo()) {
            case Cutting -> (int)(total * 0.80);
            case Bulking -> (int)(total * 1.15);
        };

        plano.definirCaloriasObjetivo(meta);
    }
}
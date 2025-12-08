package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;
// Removido @Component - será criado manualmente na configuração
public class RegistroInfoDecorator extends PlanoNutricionalDecorator {

    public RegistroInfoDecorator(PlanoNutricionalApplicationService next) {
        super(next);
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.criar(command);
        System.out.println("[LOG] Plano criado: " + plano.getId().getId());
        return plano;
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.modificar(command);
        System.out.println("[LOG] Plano modificado: " + plano.getId().getId());
        return plano;
    }
}

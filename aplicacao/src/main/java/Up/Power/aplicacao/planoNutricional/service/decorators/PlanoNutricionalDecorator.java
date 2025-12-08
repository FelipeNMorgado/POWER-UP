package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;

public abstract class PlanoNutricionalDecorator implements PlanoNutricionalApplicationService {

    protected final PlanoNutricionalApplicationService next;

    protected PlanoNutricionalDecorator(PlanoNutricionalApplicationService next) {
        this.next = next;
    }
}

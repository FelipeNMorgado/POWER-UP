package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;

public abstract class PlanoNutricionalDecorator implements PlanoNutricionalApplicationService {

    protected final PlanoNutricionalApplicationService next;

    protected PlanoNutricionalDecorator(PlanoNutricionalApplicationService next) {
        this.next = next;
    }

    @Override
    public java.util.List<PlanoNutricional> listarPorUsuario(String usuarioEmail) {
        return next.listarPorUsuario(usuarioEmail);
    }

    @Override
    public void excluir(int planoId) {
        next.excluir(planoId);
    }
}

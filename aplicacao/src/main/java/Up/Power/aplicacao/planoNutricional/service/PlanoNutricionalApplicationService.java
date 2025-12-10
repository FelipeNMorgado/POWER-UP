package Up.Power.aplicacao.planoNutricional.service;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;

public interface PlanoNutricionalApplicationService {
    PlanoNutricional criar(CriarPlanoNutricionalCommand command);
    PlanoNutricional modificar(ModificarPlanoNutricionalCommand command);
    java.util.List<PlanoNutricional> listarPorUsuario(String usuarioEmail);
    void excluir(int planoId);
}

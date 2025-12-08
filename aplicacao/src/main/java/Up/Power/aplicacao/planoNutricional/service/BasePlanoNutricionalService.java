package Up.Power.aplicacao.planoNutricional.service;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.planoNutricional.PlanoNutricionalRepository;
import Up.Power.refeicao.RefeicaoId;
import org.springframework.stereotype.Service;

@Service("basePlanoNutricionalService")
public class BasePlanoNutricionalService implements PlanoNutricionalApplicationService {

    private final PlanoNutricionalRepository repository;

    public BasePlanoNutricionalService(PlanoNutricionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        PlanoNutricional plano =
                new PlanoNutricional(
                        new PlanoNId(0),
                        command.objetivo()
                );

        plano.adicionarRefeicaoList(
                command.refeicoesIds()
                        .stream()
                        .map(RefeicaoId::new)
                        .toList()
        );

        repository.salvar(plano);
        return plano;
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = repository.obter(new PlanoNId(command.planoId()));

        plano.adicionarRefeicaoList(
                command.refeicoesIds()
                        .stream()
                        .map(RefeicaoId::new)
                        .toList()
        );

        repository.salvar(plano);
        return plano;
    }
}
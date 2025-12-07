package Up.Power.infraestrutura.persistencia.jpa.refeicao;

import Up.Power.Refeicao;
import Up.Power.alimento.AlimentoId;
import Up.Power.refeicao.RefeicaoId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RefeicaoMapper {

    public RefeicaoJpa toEntity(Refeicao refeicao) {
        List<Integer> alimentosIds = refeicao.getAlimentos().stream()
                .map(AlimentoId::getId)
                .collect(Collectors.toList());

        return new RefeicaoJpa(
                refeicao.getId() != null ? refeicao.getId().getId() : null,
                refeicao.getTipo(),
                alimentosIds,
                refeicao.getCaloriasTotais(),
                refeicao.getInicio(),
                refeicao.getFim()
        );
    }

    public Refeicao toDomain(RefeicaoJpa entity) {
        Refeicao refeicao = new Refeicao(
                new RefeicaoId(entity.getId()),
                entity.getTipo()
        );

        if (entity.getAlimentosIds() != null) {
            for (Integer alimentoId : entity.getAlimentosIds()) {
                refeicao.adicionarAlimento(new AlimentoId(alimentoId));
            }
        }

        refeicao.setCaloriasTotais(entity.getCaloriasTotais());
        refeicao.setInicio(entity.getInicio());
        refeicao.setFim(entity.getFim());

        return refeicao;
    }
}


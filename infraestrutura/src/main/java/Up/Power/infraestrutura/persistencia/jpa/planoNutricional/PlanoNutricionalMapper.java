package Up.Power.infraestrutura.persistencia.jpa.planoNutricional;

import Up.Power.PlanoNutricional;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.planoNutricional.Objetivo;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.EstadoPlano;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanoNutricionalMapper {

    public PlanoNutricional toDomain(PlanoNutricionalJpa jpa) {
        PlanoNutricional dominio =
                new PlanoNutricional(
                        new PlanoNId(jpa.getId()),
                        jpa.getObjetivo()
                );

        dominio.adicionarRefeicaoList(
                jpa.getRefeicoes().stream()
                        .map(RefeicaoId::new)
                        .collect(Collectors.toList())
        );

        dominio.definirCaloriasObjetivo(jpa.getCaloriasObjetivo());

        return dominio;
    }

    public PlanoNutricionalJpa toEntity(PlanoNutricional plano) {

        List<Integer> refeicoesIds = plano.getRefeicoes().stream()
                .map(RefeicaoId::getId)
                .collect(Collectors.toList());

        return new PlanoNutricionalJpa(
                plano.getId().getId(),
                plano.getObjetivo(),
                EstadoPlano.Ativo,
                refeicoesIds,
                plano.getCaloriasTotais(),
                plano.getCaloriasObjetivo()
        );
    }
}

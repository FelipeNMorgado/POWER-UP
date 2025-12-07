package Up.Power.infraestrutura.persistencia.jpa.alimento;

import Up.Power.Alimento;
import Up.Power.alimento.AlimentoId;
import org.springframework.stereotype.Component;

@Component
public class AlimentoMapper {

    public AlimentoJpa toEntity(Alimento alimento) {
        return new AlimentoJpa(
                alimento.getAlimento() != null ? alimento.getAlimento().getId() : null,
                alimento.getCategoria(),
                alimento.getNome(),
                alimento.getCalorias(),
                alimento.getQuantidade()
        );
    }

    public Alimento toDomain(AlimentoJpa entity) {
        return new Alimento(
                new AlimentoId(entity.getId()),
                entity.getCategoria(),
                entity.getNome(),
                entity.getCalorias(),
                entity.getQuantidade()
        );
    }
}


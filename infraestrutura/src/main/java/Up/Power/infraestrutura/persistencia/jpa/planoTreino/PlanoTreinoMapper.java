package Up.Power.infraestrutura.persistencia.jpa.planoTreino;

import Up.Power.*;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.infraestrutura.persistencia.jpa.treino.TreinoJpa;
import Up.Power.infraestrutura.persistencia.jpa.treino.TreinoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanoTreinoMapper {

    // DOMAIN → ENTITY
    // Nota: Os treinos devem ser salvos primeiro na tabela treino antes de associar ao plano
    public PlanoTreinoJpa toEntity(PlanoTreino plano, List<TreinoJpa> treinosJpa) {
        return new PlanoTreinoJpa(
                plano.getId().getId(),
                plano.getUsuarioEmail().getCaracteres(),
                plano.getNome(),
                plano.getEstadoPlano(),
                plano.getDias(),
                treinosJpa
        );
    }

    // ENTITY → DOMAIN
    public PlanoTreino toDomain(PlanoTreinoJpa entity) {
        PlanoTreino plano = new PlanoTreino(
                new PlanoTId(entity.getId()),
                new Email(entity.getUsuarioEmail()),
                entity.getNome()
        );

        plano.setDias(entity.getDias());

        // Converter TreinoJpa de volta para Treino do domínio
        List<Treino> treinosDominio = entity.getTreinos().stream()
                .map(TreinoMapper::toDomain)
                .collect(Collectors.toList());

        plano.setTreinos(treinosDominio);
        plano.alterarEstadoPlano(entity.getEstado());

        return plano;
    }
}

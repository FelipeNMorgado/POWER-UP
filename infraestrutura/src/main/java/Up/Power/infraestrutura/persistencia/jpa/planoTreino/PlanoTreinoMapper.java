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
        // Se o ID for 0, passar null para que o banco gere o ID automaticamente
        Integer id = (plano.getId() != null && plano.getId().getId() != 0) 
                ? plano.getId().getId() 
                : null;
        return new PlanoTreinoJpa(
                id,
                plano.getUsuarioEmail().getCaracteres(),
                plano.getNome(),
                plano.getEstadoPlano(),
                plano.getDias(),
                treinosJpa
        );
    }

    // ENTITY → DOMAIN
    public PlanoTreino toDomain(PlanoTreinoJpa entity) {
        // Se o ID for null, usar 0 (será tratado como novo plano)
        int planoTId = (entity.getId() != null) ? entity.getId() : 0;
        PlanoTreino plano = new PlanoTreino(
                new PlanoTId(planoTId),
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

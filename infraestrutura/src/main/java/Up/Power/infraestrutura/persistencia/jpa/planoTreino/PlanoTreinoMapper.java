package Up.Power.infraestrutura.persistencia.jpa.planoTreino;

import Up.Power.*;
import Up.Power.exercicio.ExercicioId;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TreinoId;

import java.util.List;
import java.util.stream.Collectors;

class PlanoTreinoMapper {

    // DOMAIN → ENTITY
    static PlanoTreinoJpa toEntity(PlanoTreino plano) {
        return new PlanoTreinoJpa(
                plano.getId().getId(),
                plano.getUsuarioEmail().getCaracteres(),
                plano.getNome(),
                plano.getEstadoPlano(),
                plano.getDias(),
                plano.getTreinos().stream()
                        .map(t -> t.getId().getId())
                        .toList()
        );
    }

    // ENTITY → DOMAIN
    static PlanoTreino toDomain(PlanoTreinoJpa entity) {

        PlanoTreino plano = new PlanoTreino(
                new PlanoTId(entity.getId()),
                new Email(entity.getUsuarioEmail()),
                entity.getNome()
        );

        plano.setDias(entity.getDias());

        List<Treino> treinosDominio = entity.getTreinos().stream()
                .map(idInt -> new Treino(
                        new TreinoId(idInt),
                        null,
                        null
                ))
                .toList();

        plano.setTreinos(treinosDominio);

        plano.alterarEstadoPlano(entity.getEstado());

        return plano;
    }
}



// =====================================
// =========== DOMAIN REPOSITORY =========
// =====================================



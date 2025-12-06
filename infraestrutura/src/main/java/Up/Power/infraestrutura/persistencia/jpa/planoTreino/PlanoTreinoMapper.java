package Up.Power.infraestrutura.persistencia.jpa.planoTreino;

import Up.Power.*;
import Up.Power.exercicio.ExercicioId;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanoTreinoMapper {

    // DOMAIN → ENTITY
    public PlanoTreinoJpa toEntity(PlanoTreino plano) {
        List<TreinoEmbedded> treinosEmbedded = plano.getTreinos().stream()
                .map(treino -> new TreinoEmbedded(
                        treino.getId().getId(),
                        treino.getExercicio() != null ? treino.getExercicio().getId() : null,
                        treino.getTipo(),
                        treino.getRepeticoes(),
                        treino.getPeso(),
                        treino.getSeries(),
                        treino.getRecordeCarga()
                ))
                .collect(Collectors.toList());

        return new PlanoTreinoJpa(
                plano.getId().getId(),
                plano.getUsuarioEmail().getCaracteres(),
                plano.getNome(),
                plano.getEstadoPlano(),
                plano.getDias(),
                treinosEmbedded
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

        // Converter treinos embedded de volta para Treino do domínio
        List<Treino> treinosDominio = entity.getTreinos().stream()
                .map(embedded -> {
                    Treino treino = new Treino(
                            new TreinoId(embedded.getTreinoId()),
                            embedded.getExercicioId() != null ? new ExercicioId(embedded.getExercicioId()) : null,
                            embedded.getTipo() != null ? embedded.getTipo() : TipoTreino.Peso
                    );
                    
                    if (embedded.getRepeticoes() != null) {
                        treino.setRepeticoes(embedded.getRepeticoes());
                    }
                    if (embedded.getPeso() != null) {
                        treino.setPeso(embedded.getPeso());
                    }
                    if (embedded.getSeries() != null) {
                        treino.setSeries(embedded.getSeries());
                    }
                    if (embedded.getRecordeCarga() != null) {
                        treino.setRecordeCarga(embedded.getRecordeCarga());
                    }
                    
                    return treino;
                })
                .collect(Collectors.toList());

        plano.setTreinos(treinosDominio);
        plano.alterarEstadoPlano(entity.getEstado());

        return plano;
    }
}

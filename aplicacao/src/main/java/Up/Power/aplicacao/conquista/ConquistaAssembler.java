package Up.Power.aplicacao.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;
import org.springframework.stereotype.Component;

@Component
public class ConquistaAssembler {

    public Conquista criarDominio(
            Integer exercicioId,
            Integer treinoId,
            String nome,
            String descricao
    ) {
        return new Conquista(
                new ConquistaId(0),         // criado com ID 0 → JPA gera ID real
                new ExercicioId(exercicioId),
                new TreinoId(treinoId),
                descricao,
                nome
        );
    }

    public ConquistaResumo toResumo(
            Conquista conquista,
            boolean concluida,
            String badgeAtual
    ) {
        return new ConquistaResumo(
                conquista.getId().getId(),
                conquista.getExercicio().getId(),
                conquista.getTreino().getId(),
                conquista.getNome(),
                conquista.getDescricao(),
                concluida,
                badgeAtual
        );
    }
}

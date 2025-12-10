package Up.Power.infraestrutura.persistencia.jpa.consquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

public class ConquistaMapper {

    public static ConquistaJpa toEntity(Conquista c) {
        ConquistaJpa entity = new ConquistaJpa(
                c.getId() == null ? null : c.getId().getId(),
                c.getExercicio().getId(),
                c.getTreino().getId(),
                c.getDescricao(),
                c.getNome()
        );
        // Mapear critérios
        entity.setPesoMinimo(c.getPesoMinimo());
        entity.setAtributoMinimo(c.getAtributoMinimo());
        entity.setTipoAtributo(c.getTipoAtributo());
        entity.setRepeticoesMinimas(c.getRepeticoesMinimas());
        entity.setSeriesMinimas(c.getSeriesMinimas());
        return entity;
    }

    public static Conquista toDomain(ConquistaJpa e) {
        Conquista conquista = new Conquista(
                new ConquistaId(e.getId()),
                new ExercicioId(e.getExercicioId()),
                new TreinoId(e.getTreinoId()),
                e.getDescricao(),
                e.getNome()
        );
        // Mapear critérios
        conquista.setPesoMinimo(e.getPesoMinimo());
        conquista.setAtributoMinimo(e.getAtributoMinimo());
        conquista.setTipoAtributo(e.getTipoAtributo());
        conquista.setRepeticoesMinimas(e.getRepeticoesMinimas());
        conquista.setSeriesMinimas(e.getSeriesMinimas());
        return conquista;
    }
}
package Up.Power.infraestrutura.persistencia.jpa.treino;

import Up.Power.Treino;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TreinoMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // DOMAIN → ENTITY
    public static TreinoJpa toEntity(Treino domain) {
        // Se o ID for 0 ou null, passar null para que o banco gere o ID automaticamente
        Integer id = (domain.getId() != null && domain.getId().getId() != 0) 
                ? domain.getId().getId() 
                : null;
        
        TreinoJpa entity = new TreinoJpa();
        entity.setId(id); // null para novos treinos
        entity.setExercicioId(domain.getExercicio().getId());
        entity.setTipo(domain.getTipo());
        
        // Converter LocalDateTime para String (HH:mm:ss)
        if (domain.getTempo() != null) {
            String tempoStr = domain.getTempo().format(TIME_FORMATTER);
            entity.setTempo(tempoStr);
        } else {
            entity.setTempo(null);
        }
        
        entity.setDistancia(domain.getDistancia());
        entity.setRepeticoes(domain.getRepeticoes());
        entity.setPeso(domain.getPeso());
        entity.setSeries(domain.getSeries());
        entity.setRecordeCarga(domain.getRecordeCarga());
        
        return entity;
    }

    // ENTITY → DOMAIN
    public static Treino toDomain(TreinoJpa entity) {
        // Se o ID for null, usar 0 (será tratado como novo treino)
        int treinoId = (entity.getId() != null) ? entity.getId() : 0;
        Treino domain = new Treino(
                new TreinoId(treinoId),
                new ExercicioId(entity.getExercicioId()),
                entity.getTipo()
        );

        // Converter String (HH:mm:ss) para LocalDateTime usando data base 1970-01-01
        if (entity.getTempo() != null && !entity.getTempo().isEmpty()) {
            try {
                // Parse do formato HH:mm:ss
                String[] partes = entity.getTempo().split(":");
                int horas = Integer.parseInt(partes[0]);
                int minutos = Integer.parseInt(partes[1]);
                int segundos = partes.length > 2 ? Integer.parseInt(partes[2]) : 0;
                LocalDateTime tempo = LocalDateTime.of(1970, 1, 1, horas, minutos, segundos);
                domain.setTempo(tempo);
            } catch (Exception e) {
                // Se falhar o parse, deixar null
                domain.setTempo(null);
            }
        } else {
            domain.setTempo(null);
        }
        
        domain.setDistancia(entity.getDistancia());
        domain.setRepeticoes(entity.getRepeticoes());
        domain.setPeso(entity.getPeso());
        domain.setSeries(entity.getSeries());
        domain.setRecordeCarga(entity.getRecordeCarga());

        return domain;
    }
}

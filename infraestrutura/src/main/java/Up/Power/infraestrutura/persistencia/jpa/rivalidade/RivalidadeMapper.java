package Up.Power.infraestrutura.persistencia.jpa.rivalidade;

import Up.Power.Rivalidade;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import org.springframework.stereotype.Component;

@Component
public class RivalidadeMapper {

    public RivalidadeJpa toEntity(Rivalidade domain) {
        RivalidadeJpa entity = new RivalidadeJpa();

        if (domain.getId() != null)
            entity.setId(domain.getId().getId());

        entity.setPerfil1Id(domain.getPerfil1().getId());
        entity.setPerfil2Id(domain.getPerfil2().getId());
        entity.setExercicioId(domain.getExercicio().getId());
        entity.setStatus(domain.getStatus());
        entity.setDataConvite(domain.getDataConvite());
        entity.setInicio(domain.getInicio());
        entity.setFim(domain.getFim());

        return entity;
    }

    public Rivalidade toDomain(RivalidadeJpa entity) {
        // Usar o construtor completo que aceita todos os campos, incluindo status
        // Isso evita chamar métodos de transição de estado que têm validações
        Rivalidade domain = new Rivalidade(
                new RivalidadeId(entity.getId()),
                new PerfilId(entity.getPerfil1Id()),
                new PerfilId(entity.getPerfil2Id()),
                new ExercicioId(entity.getExercicioId()),
                entity.getDataConvite(),
                entity.getInicio(),
                entity.getFim(),
                entity.getStatus()
        );

        return domain;
    }
}

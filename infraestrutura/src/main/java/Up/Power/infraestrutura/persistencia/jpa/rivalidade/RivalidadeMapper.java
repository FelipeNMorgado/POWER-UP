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
        Rivalidade domain = new Rivalidade(
                new PerfilId(entity.getPerfil1Id()),
                new PerfilId(entity.getPerfil2Id()),
                new ExercicioId(entity.getExercicioId())
        );

        domain.setId(new RivalidadeId(entity.getId()));

        // aplicar estado real vindo do banco
        if (entity.getStatus() != null) {
            switch (entity.getStatus()) {
                case ATIVA -> domain.aceitar();
                case RECUSADA -> domain.recusar();
                case FINALIZADA -> domain.finalizar();
                case PENDENTE -> {} // nada
            }
        }

        return domain;
    }
}

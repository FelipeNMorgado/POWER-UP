package Up.Power.infraestrutura.persistencia.jpa.frequencia;

import Up.Power.Frequencia;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.perfil.PerfilId;
import Up.Power.treino.TreinoId;
import Up.Power.planoTreino.PlanoTId;
import org.springframework.stereotype.Component;

@Component
public class FrequenciaMapper {

    public FrequenciaJpa toEntity(Frequencia domain) {
        FrequenciaJpa entity = new FrequenciaJpa();

        // Só define o ID se for diferente de 0 (nova entidade tem ID 0)
        if (domain.getId() != null && domain.getId().getId() != 0)
            entity.setId(domain.getId().getId());

        if (domain.getPerfil() != null)
            entity.setPerfilId(domain.getPerfil().getId());

        if (domain.getTreino() != null)
            entity.setTreinoId(domain.getTreino().getId());

        if (domain.getPlanoTId() != null)
            entity.setPlanoTreinoId(domain.getPlanoTId().getId());

        entity.setDataDePresenca(domain.getDataDePresenca());
        entity.setFoto(domain.getFoto());

        return entity;
    }

    public Frequencia toDomain(FrequenciaJpa entity) {
        Frequencia domain = new Frequencia(
                new FrequenciaId(entity.getId()),
                new PerfilId(entity.getPerfilId()),
                new TreinoId(entity.getTreinoId()),
                entity.getDataDePresenca()
        );

        if (entity.getPlanoTreinoId() != null)
            domain.setPlanoT(new PlanoTId(entity.getPlanoTreinoId()));

        domain.setFoto(entity.getFoto());

        return domain;
    }
}

package Up.Power.infraestrutura.persistencia.jpa.duelo;

import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;
import Up.Power.duelo.DueloId;

public class DueloMapper {

    public static DueloJpa toEntity(Duelo duelo) {
        return new DueloJpa(
                duelo.getId() == null ? null : duelo.getId().getId(),
                duelo.getAvatar1().getId(),
                duelo.getAvatar2().getId(),
                duelo.getResultado(),
                duelo.getDataDuelo()
        );
    }

    public static Duelo toDomain(DueloJpa entity) {
        return new Duelo(
                new DueloId(entity.getId()),
                new AvatarId(entity.getAvatar1Id()),
                new AvatarId(entity.getAvatar2Id()),
                entity.getResultado(),
                entity.getDataDuelo()
        );
    }
}

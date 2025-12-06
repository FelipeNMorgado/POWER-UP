package Up.Power.infraestrutura.persistencia.jpa.avatar;

import Up.Power.Acessorio;
import Up.Power.Avatar;
import Up.Power.acessorio.AcessorioId;
import Up.Power.avatar.AvatarId;
import Up.Power.perfil.PerfilId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvatarMapper {

    public static AvatarJpa toEntity(Avatar avatar) {
        List<Integer> acessorioIds = avatar.getAcessorios().stream()
                .map(a -> a.getId().getId())
                .collect(Collectors.toList());

        return new AvatarJpa(
                avatar.getId() == null ? null : avatar.getId().getId(),
                avatar.getPerfil().getId(),
                acessorioIds,
                avatar.getNivel(),
                avatar.getExperiencia(),
                avatar.getDinheiro(),
                avatar.getForca()
        );
    }

    public static Avatar toDomain(AvatarJpa entity) {
        Avatar avatar = new Avatar(
                new AvatarId(entity.getId()),
                new PerfilId(entity.getPerfilId())
        );
        
        avatar.setNivel(entity.getNivel());
        avatar.setExperiencia(entity.getExperiencia());
        avatar.setDinheiro(entity.getDinheiro());
        avatar.setForca(entity.getForca());
        
        // Adicionar acessórios ao avatar através da lista retornada pelo getter
        if (entity.getAcessorioIds() != null && !entity.getAcessorioIds().isEmpty()) {
            List<Acessorio> acessorios = entity.getAcessorioIds().stream()
                    .map(id -> new Acessorio(new AcessorioId(id), "", 0, "", ""))
                    .collect(Collectors.toList());
            avatar.getAcessorios().clear();
            avatar.getAcessorios().addAll(acessorios);
        }

        return avatar;
    }
}


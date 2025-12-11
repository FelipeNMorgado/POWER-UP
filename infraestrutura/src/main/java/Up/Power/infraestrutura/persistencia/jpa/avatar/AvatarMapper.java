package Up.Power.infraestrutura.persistencia.jpa.avatar;

import Up.Power.Acessorio;
import Up.Power.Avatar;
import Up.Power.acessorio.AcessorioId;
import Up.Power.acessorio.AcessorioRepository;
import Up.Power.avatar.AvatarId;
import Up.Power.perfil.PerfilId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvatarMapper {

    public static AvatarJpa toEntity(Avatar avatar) {
        // Garante que itens no inventário tenham entrada na flag (default false)
        avatar.getAcessorios().forEach(acc -> {
            avatar.getAcessoriosEquipados().putIfAbsent(acc.getId().getId(), false);
        });

        // Mapear inventário com flag de equipado.
        List<AvatarAcessorioEmbeddable> acessorios = avatar.getAcessoriosEquipados().entrySet().stream()
                .map(e -> new AvatarAcessorioEmbeddable(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        // Se o ID for null ou 0, trata como null para que o Hibernate gere um novo ID
        Integer id = null;
        if (avatar.getId() != null && avatar.getId().getId() > 0) {
            id = avatar.getId().getId();
        }

        return new AvatarJpa(
                id,
                avatar.getPerfil().getId(),
                acessorios,
                avatar.getNivel(),
                avatar.getExperiencia(),
                avatar.getDinheiro(),
                avatar.getForca()
        );
    }

    public static Avatar toDomain(AvatarJpa entity, AcessorioRepository acessorioRepository) {
        Avatar avatar = new Avatar(
                new AvatarId(entity.getId()),
                new PerfilId(entity.getPerfilId())
        );
        
        avatar.setNivel(entity.getNivel() != null ? entity.getNivel() : 1);
        avatar.setExperiencia(entity.getExperiencia() != null ? entity.getExperiencia() : 0);
        avatar.setDinheiro(entity.getDinheiro() != null ? entity.getDinheiro() : 0);
        avatar.setForca(entity.getForca() != null ? entity.getForca() : 0);
        
        // Carregar inventário com flag de equipado
        if (entity.getAcessorios() != null && !entity.getAcessorios().isEmpty()) {
            entity.getAcessorios().forEach(emb -> {
                var acessorioOpt = acessorioRepository.findById(new AcessorioId(emb.getAcessorioId()));
                acessorioOpt.ifPresent(ac -> {
                    avatar.adicionarAcessorio(ac, Boolean.TRUE.equals(emb.getEquipado()));
                });
            });
        }

        return avatar;
    }
}


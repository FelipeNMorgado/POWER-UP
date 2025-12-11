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
        List<Integer> acessorioIds = avatar.getAcessorios().stream()
                .map(a -> a.getId().getId())
                .collect(Collectors.toList());

        // Se o ID for null ou 0, trata como null para que o Hibernate gere um novo ID
        Integer id = null;
        if (avatar.getId() != null && avatar.getId().getId() > 0) {
            id = avatar.getId().getId();
        }

        return new AvatarJpa(
                id,
                avatar.getPerfil().getId(),
                acessorioIds,
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
        
        // Carregar acessórios completos do banco de dados
        if (entity.getAcessorioIds() != null && !entity.getAcessorioIds().isEmpty()) {
            System.out.println("AvatarMapper - Carregando " + entity.getAcessorioIds().size() + " acessórios para o avatar ID: " + entity.getId());
            List<Acessorio> acessorios = entity.getAcessorioIds().stream()
                    .map(id -> {
                        System.out.println("AvatarMapper - Buscando acessório ID: " + id);
                        var acessorioOpt = acessorioRepository.findById(new AcessorioId(id));
                        if (acessorioOpt.isPresent()) {
                            Acessorio acessorio = acessorioOpt.get();
                            System.out.println("AvatarMapper - Acessório encontrado - ID: " + acessorio.getId().getId() + 
                                             ", Nome: " + acessorio.getNome() + 
                                             ", Preço: " + acessorio.getPreco());
                            return acessorioOpt;
                        } else {
                            System.out.println("AvatarMapper - Acessório ID " + id + " não encontrado no banco!");
                            return java.util.Optional.<Acessorio>empty();
                        }
                    })
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toList());
            System.out.println("AvatarMapper - Total de acessórios carregados: " + acessorios.size());
            avatar.getAcessorios().clear();
            avatar.getAcessorios().addAll(acessorios);
        } else {
            System.out.println("AvatarMapper - Nenhum acessório encontrado para o avatar ID: " + entity.getId());
        }

        return avatar;
    }
}


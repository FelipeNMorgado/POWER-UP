package Up.Power.aplicacao.avatar;

import Up.Power.Acessorio;
import Up.Power.Avatar;

import java.util.List;
import java.util.stream.Collectors;

public final class AvatarResumoAssembler {

    private AvatarResumoAssembler() {
    }

    public static AvatarResumo toResumo(Avatar avatar) {
        System.out.println("AvatarResumoAssembler - Total de acessórios no avatar: " + avatar.getAcessorios().size());
        
        List<AcessorioResumo> acessoriosResumo = avatar.getAcessorios().stream()
                .map(acessorio -> {
                    boolean equipado = avatar.getAcessoriosEquipados()
                            .getOrDefault(acessorio.getId().getId(), false);
                    return new AcessorioResumo(
                            acessorio.getId().getId(),
                            acessorio.getIcone() != null ? acessorio.getIcone() : "",
                            acessorio.getImagem() != null ? acessorio.getImagem() : "",
                            acessorio.getNome() != null ? acessorio.getNome() : "",
                            acessorio.getPreco(),
                            acessorio.getQualidade(),
                            acessorio.getCategoria(),
                            acessorio.getSubcategoria(),
                            equipado
                    );
                })
                .collect(Collectors.toList());

        return new AvatarResumo(
                // Mapeia os IDs (que são Value Objects)
                avatar.getId() != null ? avatar.getId().getId() : null,
                avatar.getPerfil() != null ? avatar.getPerfil().getId() : null,
                // Mapeia os atributos primitivos
                avatar.getNivel(),
                avatar.getExperiencia(),
                avatar.getForca(),
                avatar.getDinheiro(),
                acessoriosResumo
        );
    }
}
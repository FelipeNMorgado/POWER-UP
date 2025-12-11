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
                    System.out.println("Mapeando acessório - ID: " + acessorio.getId().getId() + 
                                     ", Nome: " + acessorio.getNome() + 
                                     ", Preço: " + acessorio.getPreco() +
                                     ", Ícone: " + (acessorio.getIcone() != null ? acessorio.getIcone() : "null") +
                                     ", Imagem: " + (acessorio.getImagem() != null && !acessorio.getImagem().isEmpty() ? "presente" : "vazia"));
                    return new AcessorioResumo(
                            acessorio.getId().getId(),
                            acessorio.getIcone() != null ? acessorio.getIcone() : "",
                            acessorio.getImagem() != null ? acessorio.getImagem() : "",
                            acessorio.getNome() != null ? acessorio.getNome() : "",
                            acessorio.getPreco()
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
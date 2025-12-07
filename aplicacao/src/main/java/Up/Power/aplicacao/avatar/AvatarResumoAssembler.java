package Up.Power.aplicacao.avatar;

import Up.Power.Avatar;

public final class AvatarResumoAssembler {

    private AvatarResumoAssembler() {
    }

    public static AvatarResumo toResumo(Avatar avatar) {
        return new AvatarResumo(
                // Mapeia os IDs (que são Value Objects)
                avatar.getId() != null ? avatar.getId().getId() : null,
                avatar.getPerfil() != null ? avatar.getPerfil().getId() : null,
                // Mapeia os atributos primitivos
                avatar.getNivel(),
                avatar.getExperiencia(),
                avatar.getForca(),
                avatar.getDinheiro()
        );
    }
}
package Up.Power.mocks;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.perfil.PerfilId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AvatarMock implements AvatarRepository {

    private final Map<AvatarId, Avatar> bancoEmMemoria;

    public AvatarMock() {
        this.bancoEmMemoria = new HashMap<>();
    }

    @Override
    public void save(Avatar avatar) {
        if (avatar == null || avatar.getId() == null) {
            throw new IllegalArgumentException("Avatar e seu ID não podem ser nulos para salvar.");
        }
        bancoEmMemoria.put(avatar.getId(), avatar);
    }

    @Override
    public Optional<Avatar> findById(AvatarId id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    @Override
    public Optional<Avatar> findByPerfilId(PerfilId perfilId) {
        return bancoEmMemoria.values().stream()
                .filter(avatar -> avatar.getPerfil() != null && avatar.getPerfil().equals(perfilId))
                .findFirst();
    }

    /**
     * Método para limpar o banco de dados em memória antes de cada cenário de teste.
     */
    public void deleteAll() {
        bancoEmMemoria.clear();
    }
}
package Up.Power.mocks;

import Up.Power.Avatar;
import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.perfil.PerfilId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AvatarMock implements AvatarRepository {

    // Simula a tabela 'avatar' do banco de dados, usando o ID do avatar como chave.
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

    /**
     * NOVO E ESSENCIAL: Encontra o avatar que pertence a um determinado perfil.
     * Isso permite que o DueloService encontre os avatares dos duelistas
     * a partir dos IDs de seus perfis.
     */
    @Override
    public Optional<Avatar> findByPerfilId(PerfilId perfilId) {
        // Itera sobre todos os avatares em memória
        return bancoEmMemoria.values().stream()
                // Filtra para encontrar aquele cujo PerfilId corresponde ao fornecido
                .filter(avatar -> avatar.getPerfil().equals(perfilId))
                // Retorna o primeiro que encontrar
                .findFirst();
    }

    /**
     * Método auxiliar para limpar o banco de dados antes de cada teste.
     */
    public void deleteAll() {
        bancoEmMemoria.clear();
    }
}
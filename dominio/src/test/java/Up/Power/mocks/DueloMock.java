package Up.Power.mocks;

import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;
import Up.Power.duelo.DueloId;
import Up.Power.duelo.DueloRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DueloMock implements DueloRepository {

    // Simula a tabela 'duelo'
    private final Map<DueloId, Duelo> bancoEmMemoria;
    // Simula a sequência de auto-incremento do ID
    private final AtomicInteger sequence;

    public DueloMock() {
        this.bancoEmMemoria = new HashMap<>();
        this.sequence = new AtomicInteger(0);
    }

    @Override
    public Duelo save(Duelo duelo) {
        if (duelo == null) {
            throw new IllegalArgumentException("Duelo não pode ser nulo.");
        }

        // Se o duelo não tem ID, é uma nova inserção
        if (duelo.getId() == null) {
            int proximoId = sequence.incrementAndGet();
            DueloId novoId = new DueloId(proximoId);

            // Atribui o ID ao objeto antes de salvar (precisa de um setId na classe Duelo)
            duelo.setId(novoId);
            bancoEmMemoria.put(novoId, duelo);
        } else {
            // Se já tem ID, é uma atualização
            bancoEmMemoria.put(duelo.getId(), duelo);
        }

        return duelo;
    }

    @Override
    public Optional<Duelo> findById(DueloId id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    /**
     * NOVO E ESSENCIAL: Encontra o duelo mais recente entre dois avatares específicos.
     * Esta é a chave para implementar a regra de cooldown de 1 semana.
     */
    @Override
    public Optional<Duelo> findLastDuelBetween(AvatarId avatarId1, AvatarId avatarId2) {
        return bancoEmMemoria.values().stream()
                // Passo 1: Filtra duelos que contenham os dois avatares.
                // A verificação é feita em ambas as ordens (A vs B e B vs A).
                .filter(duelo ->
                        (duelo.getAvatar1().equals(avatarId1) && duelo.getAvatar2().equals(avatarId2)) ||
                                (duelo.getAvatar1().equals(avatarId2) && duelo.getAvatar2().equals(avatarId1))
                )
                // Passo 2: Encontra o duelo com a data mais recente.
                .max(Comparator.comparing(Duelo::getDataDuelo));
    }

    public List<Duelo> findAll() {
        return new ArrayList<>(bancoEmMemoria.values());
    }
    /**
     * Metodo auxiliar para limpar o banco de dados antes de cada teste.
     */
    public void deleteAll() {
        bancoEmMemoria.clear();
        sequence.set(0);
    }
}
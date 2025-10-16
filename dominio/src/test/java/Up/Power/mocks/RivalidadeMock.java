package Up.Power.mocks;

import Up.Power.Rivalidade;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RivalidadeMock implements RivalidadeRepository {

    // Simula a tabela do banco de dados, usando o ID como chave.
    private final Map<RivalidadeId, Rivalidade> bancoEmMemoria;

    // Simula a sequência de auto-incremento do banco de dados.
    // AtomicInteger é usado para garantir que o ID seja único mesmo em ambientes com threads.
    private final AtomicInteger sequence;

    public RivalidadeMock() {
        this.bancoEmMemoria = new HashMap<>();
        this.sequence = new AtomicInteger(0); // Começa em 0, o primeiro ID será 1.
    }

    @Override
    public Rivalidade save(Rivalidade rivalidade) {
        if (rivalidade == null) {
            throw new IllegalArgumentException("Rivalidade não pode ser nula.");
        }
        bancoEmMemoria.put(rivalidade.getId(), rivalidade);
        // Retorna o objeto com o ID, exatamente como o JPA/Hibernate faz.
        return rivalidade;
    }

    @Override
    public Optional<Rivalidade> findById(RivalidadeId id) { // <-- Mude o tipo de retorno aqui também
        Rivalidade rivalidadeEncontrada = bancoEmMemoria.get(id);
        return Optional.ofNullable(rivalidadeEncontrada); // <-- Embrulhe o resultado
    }

    public List<Rivalidade> findAll() {
        return new ArrayList<>(bancoEmMemoria.values());
    }

    public void deleteAll() {
        bancoEmMemoria.clear();
        sequence.set(0); // Reseta a sequência de IDs
    }
}
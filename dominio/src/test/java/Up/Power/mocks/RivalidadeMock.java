package Up.Power.mocks;

import Up.Power.Rivalidade;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeRepository;
import Up.Power.rivalidade.StatusRivalidade;

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
    public boolean existsActiveRivalryForPerfil(PerfilId perfilId) {
        // Itera sobre todas as rivalidades salvas
        for (Rivalidade r : bancoEmMemoria.values()) {
            // Verifica se a rivalidade está ATIVA
            if (r.getStatus() == StatusRivalidade.ATIVA) {
                // Verifica se o perfilId é um dos participantes
                if (r.getPerfil1().equals(perfilId) || r.getPerfil2().equals(perfilId)) {
                    return true; // Encontrou! Retorna true.
                }
            }
        }
        return false; // Não encontrou nenhuma rivalidade ativa para este perfil.
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
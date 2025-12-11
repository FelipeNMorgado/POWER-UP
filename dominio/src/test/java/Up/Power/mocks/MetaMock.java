package Up.Power.mocks;

import Up.Power.Meta;
import Up.Power.meta.MetaId;
import Up.Power.meta.MetaRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MetaMock implements MetaRepository {

    private final Map<MetaId, Meta> banco = new HashMap<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    @Override
    public Meta save(Meta meta) {
        if (meta.getId() == null) {
            MetaId novoId = new MetaId(seq.incrementAndGet());
        }
        banco.put(meta.getId(), meta);
        return meta;
    }

    @Override
    public Optional<Meta> findById(MetaId id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public List<Meta> findByUserId(int userId) {
        return new ArrayList<>(banco.values());
    }

    @Override
    public void delete(MetaId id) {

    }

    @Override
    public void deleteAll() {
        banco.clear();
        seq.set(0);
    }

    public List<Meta> findAll() {
        return new ArrayList<>(banco.values());
    }
}

package Up.Power.meta;

import Up.Power.Meta;
import java.util.List;
import java.util.Optional;

public interface MetaRepository {
    Meta save(Meta meta);
    Optional<Meta> findById(MetaId id);
    List<Meta> findByUserId(int userId);
    void delete(MetaId id);
    void deleteAll(); // usado pelos testes
}

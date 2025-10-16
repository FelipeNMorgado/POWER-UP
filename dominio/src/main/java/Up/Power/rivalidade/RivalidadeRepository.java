package Up.Power.rivalidade;

import Up.Power.Rivalidade;

import java.util.Optional;

public interface RivalidadeRepository {
    Rivalidade save(Rivalidade rivalidade);
    Optional<Rivalidade> findById(RivalidadeId id);


}

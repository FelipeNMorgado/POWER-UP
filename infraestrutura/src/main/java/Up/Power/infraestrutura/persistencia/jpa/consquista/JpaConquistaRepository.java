package Up.Power.infraestrutura.persistencia.jpa.consquista;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaConquistaRepository extends JpaRepository<ConquistaJpa, Integer> {
}


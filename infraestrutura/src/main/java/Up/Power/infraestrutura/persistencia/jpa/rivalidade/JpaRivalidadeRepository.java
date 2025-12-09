package Up.Power.infraestrutura.persistencia.jpa.rivalidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaRivalidadeRepository extends JpaRepository<RivalidadeJpa, Integer> {

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM RivalidadeJpa r
        WHERE (r.perfil1Id = :perfilId OR r.perfil2Id = :perfilId)
          AND r.status = 'ATIVA'
    """)
    boolean existsActiveRivalryForPerfil(@Param("perfilId") Integer perfilId);

    @Query("select r from RivalidadeJpa r where r.perfil1Id = :p or r.perfil2Id = :p")
    List<RivalidadeJpa> findByPerfilParticipante(@Param("p") Integer p);
}

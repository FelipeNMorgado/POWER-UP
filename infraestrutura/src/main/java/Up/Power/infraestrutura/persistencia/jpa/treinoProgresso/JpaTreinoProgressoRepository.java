package Up.Power.infraestrutura.persistencia.jpa.treinoProgresso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

interface JpaTreinoProgressoRepository extends JpaRepository<TreinoProgressoJpa, Integer> {
    @Query("SELECT t FROM TreinoProgressoJpa t WHERE t.perfilId = :perfilId ORDER BY t.dataRegistro ASC")
    List<TreinoProgressoJpa> findByPerfil(@Param("perfilId") Integer perfilId);

    @Query("SELECT t FROM TreinoProgressoJpa t WHERE t.perfilId = :perfilId AND t.exercicioId = :exercicioId ORDER BY t.dataRegistro ASC")
    List<TreinoProgressoJpa> findByPerfilAndExercicio(@Param("perfilId") Integer perfilId, @Param("exercicioId") Integer exercicioId);
}

@org.springframework.stereotype.Repository
class TreinoProgressoRepositoryImpl implements Up.Power.treinoProgresso.TreinoProgressoRepository {

    private final JpaTreinoProgressoRepository jpaRepository;

    public TreinoProgressoRepositoryImpl(JpaTreinoProgressoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Up.Power.treinoProgresso.TreinoProgresso salvar(Up.Power.treinoProgresso.TreinoProgresso progresso) {
        TreinoProgressoJpa entity = TreinoProgressoMapper.toEntity(progresso);
        TreinoProgressoJpa saved = jpaRepository.save(entity);
        return TreinoProgressoMapper.toDomain(saved);
    }

    @Override
    public java.util.List<Up.Power.treinoProgresso.TreinoProgresso> listarPorPerfil(Up.Power.perfil.PerfilId perfilId) {
        return jpaRepository.findByPerfil(perfilId.getId())
                .stream()
                .map(TreinoProgressoMapper::toDomain)
                .toList();
    }

    @Override
    public java.util.List<Up.Power.treinoProgresso.TreinoProgresso> listarPorPerfilEExercicio(Up.Power.perfil.PerfilId perfilId, Up.Power.exercicio.ExercicioId exercicioId) {
        return jpaRepository.findByPerfilAndExercicio(perfilId.getId(), exercicioId.getId())
                .stream()
                .map(TreinoProgressoMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(Up.Power.treinoProgresso.TreinoProgressoId id) {
        jpaRepository.deleteById(id.getId());
    }
}


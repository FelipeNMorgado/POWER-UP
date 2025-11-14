import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class AlimentoRepositoryImpl {

    @Autowired
    private AlimentoJpaRepository repositorio;

    public void salvar(Alimento alimento) {
        repositorio.save(AlimentoJpa.fromDomain(alimento));
    }

    public Optional<Alimento> obterPorId(AlimentoId id) {
        int valor = Integer.parseInt(id.toString());
        return repositorio.findById(valor).map(AlimentoJpa::toDomain);
    }
}

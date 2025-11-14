import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Repository
public class AcessorioRepositorioImpl implements AcessorioRepository {

    private final AcessorioJpaRepository repositorio;

    @Autowired
    public AcessorioRepositorioImpl(AcessorioJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void save(Acessorio acessorio) {
        AcessorioJpa entity = new AcessorioJpa(
                Integer.parseInt(acessorio.getId().toString()),
                acessorio.getIcone(),
                acessorio.getPreco(),
                acessorio.getNome(),
                acessorio.getImagem()
        );
        repositorio.save(entity);
    }

    @Override
    public Optional<Acessorio> findById(AcessorioId id) {
        return repositorio.findById(Integer.parseInt(id.toString()))
                .map(entity -> new Acessorio(
                        new AcessorioId(String.valueOf(entity.getId())),
                        entity.getIcone(),
                        entity.getPreco(),
                        entity.getNome(),
                        entity.getImagem()
                ));
    }
}

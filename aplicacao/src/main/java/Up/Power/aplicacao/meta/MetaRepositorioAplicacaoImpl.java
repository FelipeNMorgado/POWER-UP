package Up.Power.aplicacao.meta;

import Up.Power.Meta;
import Up.Power.meta.MetaId;
import Up.Power.meta.MetaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MetaRepositorioAplicacaoImpl implements MetaRepositorioAplicacao {

    private final MetaRepository metaRepository;

    public MetaRepositorioAplicacaoImpl(MetaRepository metaRepository) {
        this.metaRepository = metaRepository;
    }

    @Override
    public Optional<Meta> obterPorId(MetaId id) {
        return metaRepository.findById(id);
    }

    @Override
    public List<Meta> obterPorUsuario(int userId) {
        return metaRepository.findByUserId(userId);
    }

    @Override
    public Meta salvar(Meta meta) {
        return metaRepository.save(meta);
    }

    @Override
    public void deletar(MetaId id) {
        metaRepository.delete(id);
    }
}
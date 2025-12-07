package Up.Power.aplicacao.meta;

import Up.Power.Meta;
import Up.Power.meta.MetaId;

import java.util.List;
import java.util.Optional;

public interface MetaRepositorioAplicacao {
    Optional<Meta> obterPorId(MetaId id);
    List<Meta> obterPorUsuario(int userId);
    Meta salvar(Meta meta);
}
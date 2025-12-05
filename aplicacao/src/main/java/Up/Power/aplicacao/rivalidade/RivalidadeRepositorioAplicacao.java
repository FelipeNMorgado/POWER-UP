package Up.Power.aplicacao.rivalidade;

import java.util.List;
import java.util.Optional;

public interface RivalidadeRepositorioAplicacao {
    Optional<RivalidadeResumo> obterPorId(Integer id);
    List<RivalidadeResumo> listarPorPerfil(Integer perfilId);
}

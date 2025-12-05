package Up.Power.aplicacao.rivalidade;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RivalidadeServicoAplicacao {

    private final RivalidadeRepositorioAplicacao repo;

    public RivalidadeServicoAplicacao(RivalidadeRepositorioAplicacao repo) {
        this.repo = repo;
    }

    public Optional<RivalidadeResumo> obter(Integer id) {
        return repo.obterPorId(id);
    }

    public List<RivalidadeResumo> listarPorPerfil(Integer perfilId) {
        return repo.listarPorPerfil(perfilId);
    }
}

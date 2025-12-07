package Up.Power.aplicacao.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.conquista.ConquistaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConquistaRepositorioAplicacaoImpl implements ConquistaRepositorioAplicacao {

    private final ConquistaRepository conquistaRepository;

    public ConquistaRepositorioAplicacaoImpl(ConquistaRepository conquistaRepository) {
        this.conquistaRepository = conquistaRepository;
    }

    @Override
    public void salvar(Conquista conquista) {
        conquistaRepository.salvar(conquista);
    }

    @Override
    public void marcarComoConcluida(Conquista conquista) {
        conquistaRepository.marcarComoConcluida(conquista);
    }

    @Override
    public Optional<Conquista> buscarPorId(ConquistaId id) {
        return conquistaRepository.buscarPorId(id);
    }

    @Override
    public List<Conquista> listarAtivas() {
        return conquistaRepository.listarAtivas();
    }

    @Override
    public List<Conquista> listarConcluidas() {
        return conquistaRepository.listarConcluidas();
    }

    @Override
    public void remover(ConquistaId id) {
        conquistaRepository.remover(id);
    }
}

package Up.Power.aplicacao.frequencia;

import Up.Power.Frequencia;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.frequencia.FrequenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FrequenciaServicoAplicacao {

    private final FrequenciaRepository repo;

    public FrequenciaServicoAplicacao(FrequenciaRepository repo) {
        this.repo = repo;
    }

    public FrequenciaResumo obterPorId(Integer id) {
        var freq = repo.obterFrequencia(new FrequenciaId(id), null);
        return freq == null ? null : toResumo(freq);
    }

    public List<FrequenciaResumo> listarPorPerfil(Integer perfilId) {
        return repo.listarPorPerfil(perfilId)
                .stream()
                .map(this::toResumo)
                .collect(Collectors.toList());
    }

    private FrequenciaResumo toResumo(Frequencia f) {
        return new FrequenciaResumo(
                f.getId().getId(),
                f.getPerfil().getId(),
                f.getTreino().getId(),
                f.getDataDePresenca(),
                f.getPlanoTId() != null ? f.getPlanoTId().getId() : null,
                f.getFoto()
        );
    }
}

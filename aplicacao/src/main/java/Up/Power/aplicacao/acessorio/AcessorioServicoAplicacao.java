package Up.Power.aplicacao.acessorio;

import Up.Power.Acessorio;
import Up.Power.acessorio.AcessorioId;
import Up.Power.acessorio.AcessorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcessorioServicoAplicacao {

    private final AcessorioRepository acessorioRepository;

    public AcessorioServicoAplicacao(AcessorioRepository acessorioRepository) {
        this.acessorioRepository = acessorioRepository;
    }

    public List<AcessorioResumo> listarTodos() {
        List<Acessorio> acessorios = acessorioRepository.findAll();
        System.out.println("AcessorioServicoAplicacao - Total de acessórios encontrados: " + acessorios.size());
        return acessorios.stream()
                .map(this::toResumo)
                .collect(Collectors.toList());
    }

    public Optional<AcessorioResumo> obterPorId(Integer id) {
        return acessorioRepository.findById(new AcessorioId(id))
                .map(this::toResumo);
    }

    private AcessorioResumo toResumo(Acessorio acessorio) {
        return new AcessorioResumo(
                acessorio.getId().getId(),
                acessorio.getIcone() != null ? acessorio.getIcone() : "",
                acessorio.getImagem() != null ? acessorio.getImagem() : "",
                acessorio.getNome() != null ? acessorio.getNome() : "",
                acessorio.getPreco(),
                acessorio.getQualidade(),
                acessorio.getCategoria(),
                acessorio.getSubcategoria()
        );
    }
}


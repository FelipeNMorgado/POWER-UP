package Up.Power.aplicacao.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.conquista.ConquistaService;
import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConquistaServicoAplicacao {

    private final ConquistaAssembler assembler;
    private final ConquistaService dominio;
    private final ConquistaRepository repository;
    private final PerfilRepository perfilRepository;

    public ConquistaServicoAplicacao(
            ConquistaAssembler assembler,
            ConquistaService dominio,
            ConquistaRepository repository,
            PerfilRepository perfilRepository
    ) {
        this.assembler = assembler;
        this.dominio = dominio;
        this.repository = repository;
        this.perfilRepository = perfilRepository;
    }

    public ConquistaResumo criar(
            Integer exercicioId,
            Integer treinoId,
            String nome,
            String descricao
    ) {
        Conquista conquista =
                assembler.criarDominio(exercicioId, treinoId, nome, descricao);

        dominio.adicionarConquistaAtiva(conquista);

        return assembler.toResumo(conquista, false, null);
    }

    public ConquistaResumo escolherBadge(Integer conquistaId, String badge) {

        Conquista conquista = repository.buscarPorId(new ConquistaId(conquistaId))
                .orElseThrow(() -> new RuntimeException("Conquista não encontrada"));

        boolean ok = dominio.escolherBadge(conquista, badge);

        return assembler.toResumo(
                conquista,
                ok,
                dominio.getBadgeAtual()
        );
    }

    public List<Conquista> listarConcluidas() {
        return dominio.getConquistasConcluidas();
    }

    public List<ConquistaResumo> listarTodas() {
        List<Conquista> conquistas = repository.listarAtivas();
        return conquistas.stream()
                .map(c -> assembler.toResumo(c, false, null))
                .toList();
    }

    public List<ConquistaResumo> listarPorPerfil(Integer perfilId) {
        Optional<Perfil> perfilOpt = perfilRepository.findById(new PerfilId(perfilId));
        if (perfilOpt.isEmpty()) {
            return List.of();
        }
        
        Perfil perfil = perfilOpt.get();
        return perfil.getConquistas().stream()
                .map(c -> assembler.toResumo(c, true, null))
                .collect(Collectors.toList());
    }
}

package Up.Power.aplicacao.conquista;

import Up.Power.Conquista;
import Up.Power.conquista.ConquistaId;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.conquista.ConquistaService;

import java.util.List;

public class ConquistaServicoAplicacao {

    private final ConquistaAssembler assembler;
    private final ConquistaService dominio;
    private final ConquistaRepository repository;

    public ConquistaServicoAplicacao(
            ConquistaAssembler assembler,
            ConquistaService dominio,
            ConquistaRepository repository
    ) {
        this.assembler = assembler;
        this.dominio = dominio;
        this.repository = repository;
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
}

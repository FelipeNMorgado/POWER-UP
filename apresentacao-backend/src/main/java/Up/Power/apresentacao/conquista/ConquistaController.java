package Up.Power.apresentacao.conquista;

import Up.Power.aplicacao.conquista.ConquistaServicoAplicacao;
import Up.Power.aplicacao.conquista.ConquistaResumo;
import Up.Power.Conquista;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conquistas")
public class ConquistaController {

    private final ConquistaServicoAplicacao conquistaServicoAplicacao;

    public ConquistaController(ConquistaServicoAplicacao conquistaServicoAplicacao) {
        this.conquistaServicoAplicacao = conquistaServicoAplicacao;
    }

    @PostMapping
    public ResponseEntity<ConquistaResumo> criar(@RequestBody CriarConquistaRequest request) {
        try {
            ConquistaResumo conquista = conquistaServicoAplicacao.criar(
                    request.exercicioId(),
                    request.treinoId(),
                    request.nome(),
                    request.descricao()
            );
            return ResponseEntity.ok(conquista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{conquistaId}/badge")
    public ResponseEntity<ConquistaResumo> escolherBadge(
            @PathVariable("conquistaId") Integer conquistaId,
            @RequestBody EscolherBadgeRequest request) {
        try {
            ConquistaResumo conquista = conquistaServicoAplicacao.escolherBadge(conquistaId, request.badge());
            return ResponseEntity.ok(conquista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/concluidas")
    public ResponseEntity<List<Conquista>> listarConcluidas() {
        try {
            List<Conquista> conquistas = conquistaServicoAplicacao.listarConcluidas();
            return ResponseEntity.ok(conquistas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ConquistaResumo>> listarTodas() {
        try {
            List<ConquistaResumo> conquistas = conquistaServicoAplicacao.listarTodas();
            return ResponseEntity.ok(conquistas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<ConquistaResumo>> listarPorPerfil(@PathVariable("perfilId") Integer perfilId) {
        try {
            List<ConquistaResumo> conquistas = conquistaServicoAplicacao.listarPorPerfil(perfilId);
            return ResponseEntity.ok(conquistas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/perfil/{perfilId}/todas-com-status")
    public ResponseEntity<ConquistasComStatusResponse> listarTodasComStatus(@PathVariable("perfilId") Integer perfilId) {
        try {
            List<ConquistaResumo> conquistas = conquistaServicoAplicacao.listarTodasComStatus(perfilId);
            long totalConquistadas = conquistas.stream().filter(ConquistaResumo::concluida).count();
            int totalGeral = conquistas.size();
            return ResponseEntity.ok(new ConquistasComStatusResponse(conquistas, (int) totalConquistadas, totalGeral));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DTOs de Request
    public record CriarConquistaRequest(Integer exercicioId, Integer treinoId, String nome, String descricao) {}
    public record EscolherBadgeRequest(String badge) {}
    
    // DTO de Response
    public record ConquistasComStatusResponse(
            List<ConquistaResumo> conquistas,
            int totalConquistadas,
            int totalGeral
    ) {}
}


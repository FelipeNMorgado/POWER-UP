package Up.Power.apresentacao.planoTreino;

import Up.Power.aplicacao.planoTreino.PlanoTreinoServicoAplicacao;
import Up.Power.aplicacao.planoTreino.PlanoTreinoResumo;
import Up.Power.EstadoPlano;
import Up.Power.planoTreino.Dias;
import Up.Power.treino.TipoTreino;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planos-treino")
public class PlanoTreinoController {

    private final PlanoTreinoServicoAplicacao planoTreinoServicoAplicacao;

    public PlanoTreinoController(PlanoTreinoServicoAplicacao planoTreinoServicoAplicacao) {
        this.planoTreinoServicoAplicacao = planoTreinoServicoAplicacao;
    }

    @PostMapping
    public ResponseEntity<PlanoTreinoResumo> criarPlanoTreino(@RequestBody CriarPlanoTreinoRequest request) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.criarPlanoTreino(
                    request.id(),
                    request.usuarioEmail(),
                    request.nome()
            );
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoTreinoResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.obterPorId(id);
            if (plano == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<PlanoTreinoResumo>> listarPorUsuario(@PathVariable("email") String email) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            List<PlanoTreinoResumo> planos = planoTreinoServicoAplicacao.listarPorUsuario(emailDecodificado);
            return ResponseEntity.ok(planos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{planoTId}/treinos")
    public ResponseEntity<PlanoTreinoResumo> adicionarTreino(
            @PathVariable("planoTId") Integer planoTId,
            @RequestBody AdicionarTreinoRequest request) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.adicionarTreino(
                    planoTId,
                    request.treinoId(),
                    request.exercicioId(),
                    request.tipo(),
                    request.repeticoes(),
                    request.peso(),
                    request.series(),
                    request.distancia(),
                    request.tempo()
            );
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{planoTId}/treinos/{treinoId}")
    public ResponseEntity<PlanoTreinoResumo> removerTreino(
            @PathVariable("planoTId") Integer planoTId,
            @PathVariable("treinoId") Integer treinoId) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.removerTreino(planoTId, treinoId);
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{planoTId}/treinos/{treinoId}")
    public ResponseEntity<PlanoTreinoResumo> atualizarTreino(
            @PathVariable("planoTId") Integer planoTId,
            @PathVariable("treinoId") Integer treinoId,
            @RequestBody AtualizarTreinoRequest request) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.atualizarTreino(
                    planoTId,
                    treinoId,
                    request.exercicioId(),
                    request.tipo(),
                    request.repeticoes(),
                    request.peso(),
                    request.series(),
                    request.distancia(),
                    request.tempo()
            );
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{planoTId}/dias")
    public ResponseEntity<PlanoTreinoResumo> adicionarDia(
            @PathVariable("planoTId") Integer planoTId,
            @RequestBody AdicionarDiaRequest request) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.adicionarDia(planoTId, request.dia());
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{planoTId}/dias")
    public ResponseEntity<PlanoTreinoResumo> atualizarDias(
            @PathVariable("planoTId") Integer planoTId,
            @RequestBody AtualizarDiasRequest request) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.atualizarDias(planoTId, request.dias());
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{planoTId}/estado")
    public ResponseEntity<PlanoTreinoResumo> alterarEstado(
            @PathVariable("planoTId") Integer planoTId,
            @RequestBody AlterarEstadoRequest request) {
        try {
            PlanoTreinoResumo plano = planoTreinoServicoAplicacao.alterarEstado(planoTId, request.estado());
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{planoTId}")
    public ResponseEntity<Void> excluirPlanoTreino(@PathVariable("planoTId") Integer planoTId) {
        try {
            planoTreinoServicoAplicacao.excluirPlanoTreino(planoTId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DTOs de Request
    public record CriarPlanoTreinoRequest(Integer id, String usuarioEmail, String nome) {}
    public record AdicionarTreinoRequest(Integer treinoId, Integer exercicioId, TipoTreino tipo, 
                                         Integer repeticoes, Float peso, Integer series,
                                         Float distancia, java.time.LocalDateTime tempo) {}
    public record AtualizarTreinoRequest(Integer exercicioId, TipoTreino tipo, 
                                         Integer repeticoes, Float peso, Integer series,
                                         Float distancia, java.time.LocalDateTime tempo) {}
    public record AdicionarDiaRequest(Dias dia) {}
    public record AtualizarDiasRequest(List<Dias> dias) {}
    public record AlterarEstadoRequest(EstadoPlano estado) {}
}


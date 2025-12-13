package Up.Power.apresentacao.rivalidade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.RivalidadeServicoAplicacao;

@RestController
@RequestMapping("/api/rivalidades")
public class RivalidadeController {

    private final RivalidadeServicoAplicacao rivalidadeServicoAplicacao;

    public RivalidadeController(RivalidadeServicoAplicacao rivalidadeServicoAplicacao) {
        this.rivalidadeServicoAplicacao = rivalidadeServicoAplicacao;
    }

    public record EnviarConviteRequest(int perfil1Id, int perfil2Id, int exercicioId) {}
    public record RivalidadeRequest(int rivalidadeId, int usuarioId) {}

    @PostMapping("/enviar-convite")
    public ResponseEntity<?> enviarConvite(@RequestBody EnviarConviteRequest request) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.enviar(
                request.perfil1Id, request.perfil2Id, request.exercicioId
            );
            return ResponseEntity.ok(rivalidade);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/aceitar")
    public ResponseEntity<?> aceitar(@RequestBody RivalidadeRequest request) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.aceitar(
                request.rivalidadeId, request.usuarioId
            );
            return ResponseEntity.ok(rivalidade);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/recusar")
    public ResponseEntity<RivalidadeResumo> recusar(@RequestBody RivalidadeRequest request) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.recusar(
                request.rivalidadeId, request.usuarioId
            );
            return ResponseEntity.ok(rivalidade);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cancelar")
    public ResponseEntity<?> cancelar(@RequestBody RivalidadeRequest request) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.cancelar(
                request.rivalidadeId, request.usuarioId
            );
            return ResponseEntity.ok(rivalidade);
        } catch (IllegalStateException | SecurityException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/finalizar")
    public ResponseEntity<RivalidadeResumo> finalizar(@RequestBody RivalidadeRequest request) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.finalizar(
                request.rivalidadeId, request.usuarioId
            );
            return ResponseEntity.ok(rivalidade);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<RivalidadeResumo>> listarPorPerfil(@PathVariable("perfilId") Integer perfilId) {
        try {
            List<RivalidadeResumo> rivalidades = rivalidadeServicoAplicacao.listarPorPerfil(perfilId);
            return ResponseEntity.ok(rivalidades);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{rivalidadeId}/comparacao")
    public ResponseEntity<RivalidadeServicoAplicacao.ComparacaoRivalidade> obterComparacao(
            @PathVariable("rivalidadeId") Integer rivalidadeId,
            @RequestParam("perfilId") Integer perfilId) {
        try {
            RivalidadeServicoAplicacao.ComparacaoRivalidade comparacao = 
                    rivalidadeServicoAplicacao.obterComparacao(rivalidadeId, perfilId);
            return ResponseEntity.ok(comparacao);
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


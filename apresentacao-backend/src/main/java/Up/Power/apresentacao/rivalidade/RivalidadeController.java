package Up.Power.apresentacao.rivalidade;

import Up.Power.aplicacao.rivalidade.RivalidadeServicoAplicacao;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.commands.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rivalidades")
public class RivalidadeController {

    private final RivalidadeServicoAplicacao rivalidadeServicoAplicacao;

    public RivalidadeController(RivalidadeServicoAplicacao rivalidadeServicoAplicacao) {
        this.rivalidadeServicoAplicacao = rivalidadeServicoAplicacao;
    }

    @PostMapping("/enviar-convite")
    public ResponseEntity<?> enviarConvite(@RequestBody EnviarConviteCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.enviar(command);
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
    public ResponseEntity<?> aceitar(@RequestBody AceitarRivalidadeCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.aceitar(command);
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
    public ResponseEntity<RivalidadeResumo> recusar(@RequestBody RecusarRivalidadeCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.recusar(command);
            return ResponseEntity.ok(rivalidade);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cancelar")
    public ResponseEntity<?> cancelar(@RequestBody CancelarRivalidadeCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.cancelar(command);
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
    public ResponseEntity<RivalidadeResumo> finalizar(@RequestBody FinalizarRivalidadeCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.finalizar(command);
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


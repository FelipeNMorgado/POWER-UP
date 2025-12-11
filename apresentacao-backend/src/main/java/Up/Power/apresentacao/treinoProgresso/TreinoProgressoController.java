package Up.Power.apresentacao.treinoProgresso;

import Up.Power.aplicacao.treinoProgresso.RegistrarTreinoProgressoCommand;
import Up.Power.aplicacao.treinoProgresso.TreinoProgressoResumo;
import Up.Power.aplicacao.treinoProgresso.TreinoProgressoServicoAplicacao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treinos/progresso")
@CrossOrigin
public class TreinoProgressoController {

    private final TreinoProgressoServicoAplicacao servico;

    public TreinoProgressoController(TreinoProgressoServicoAplicacao servico) {
        this.servico = servico;
    }

    @GetMapping
    public ResponseEntity<List<TreinoProgressoResumo>> listar(
            @RequestParam("perfilId") Integer perfilId,
            @RequestParam(value = "exercicioId", required = false) Integer exercicioId
    ) {
        try {
            return ResponseEntity.ok(servico.listar(perfilId, exercicioId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<TreinoProgressoResumo> registrar(@RequestBody RegistrarTreinoProgressoCommand command) {
        try {
            TreinoProgressoResumo salvo = servico.registrar(command);
            return ResponseEntity.ok(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}


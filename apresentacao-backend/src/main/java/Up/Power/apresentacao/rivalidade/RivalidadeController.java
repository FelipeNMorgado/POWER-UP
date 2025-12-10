package Up.Power.apresentacao.rivalidade;

import Up.Power.aplicacao.rivalidade.RivalidadeServicoAplicacao;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.commands.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rivalidades")
public class RivalidadeController {

    private final RivalidadeServicoAplicacao rivalidadeServicoAplicacao;

    public RivalidadeController(RivalidadeServicoAplicacao rivalidadeServicoAplicacao) {
        this.rivalidadeServicoAplicacao = rivalidadeServicoAplicacao;
    }

    @PostMapping("/enviar-convite")
    public ResponseEntity<RivalidadeResumo> enviarConvite(@RequestBody EnviarConviteCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.enviar(command);
            return ResponseEntity.ok(rivalidade);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/aceitar")
    public ResponseEntity<RivalidadeResumo> aceitar(@RequestBody AceitarRivalidadeCommand command) {
        try {
            RivalidadeResumo rivalidade = rivalidadeServicoAplicacao.aceitar(command);
            return ResponseEntity.ok(rivalidade);
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
}


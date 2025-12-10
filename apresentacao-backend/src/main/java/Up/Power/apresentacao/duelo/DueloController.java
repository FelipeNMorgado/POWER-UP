package Up.Power.apresentacao.duelo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Up.Power.aplicacao.duelo.DueloResumo;
import Up.Power.aplicacao.duelo.DueloServicoAplicacao;
import Up.Power.aplicacao.duelo.RealizarDueloCommand;

@RestController
@RequestMapping("/api/duelos")
public class DueloController {

    private final DueloServicoAplicacao dueloServicoAplicacao;

    public DueloController(DueloServicoAplicacao dueloServicoAplicacao) {
        this.dueloServicoAplicacao = dueloServicoAplicacao;
    }

    @PostMapping
    public ResponseEntity<?> realizarDuelo(@RequestBody RealizarDueloRequest request) {
        try {
            DueloResumo resumo = dueloServicoAplicacao.realizarDuelo(
                    new RealizarDueloCommand(request.desafiantePerfilId(), request.desafiadoPerfilId())
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(resumo);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao realizar duelo: " + e.getMessage()));
        }
    }
    
    private record ErrorResponse(String message) {}

    @GetMapping("/{id}")
    public ResponseEntity<DueloResumo> obterPorId(@PathVariable("id") Integer id) {
        DueloResumo resumo = dueloServicoAplicacao.obterPorId(id);
        if (resumo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/ultimo")
    public ResponseEntity<DueloResumo> ultimoEntrePerfis(
            @RequestParam("perfil1") Integer perfilId1,
            @RequestParam("perfil2") Integer perfilId2) {
        DueloResumo resumo = dueloServicoAplicacao.ultimoEntrePerfis(perfilId1, perfilId2);
        if (resumo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumo);
    }
}

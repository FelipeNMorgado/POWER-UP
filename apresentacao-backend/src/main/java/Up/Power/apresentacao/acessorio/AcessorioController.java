package Up.Power.apresentacao.acessorio;

import Up.Power.aplicacao.acessorio.AcessorioResumo;
import Up.Power.aplicacao.acessorio.AcessorioServicoAplicacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acessorios")
public class AcessorioController {

    private final AcessorioServicoAplicacao acessorioServicoAplicacao;

    public AcessorioController(AcessorioServicoAplicacao acessorioServicoAplicacao) {
        this.acessorioServicoAplicacao = acessorioServicoAplicacao;
    }

    @GetMapping
    public ResponseEntity<List<AcessorioResumo>> listarTodos() {
        try {
            List<AcessorioResumo> acessorios = acessorioServicoAplicacao.listarTodos();
            System.out.println("AcessorioController - Total de acessórios retornados: " + acessorios.size());
            return ResponseEntity.ok(acessorios);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao listar acessórios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcessorioResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            return acessorioServicoAplicacao.obterPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


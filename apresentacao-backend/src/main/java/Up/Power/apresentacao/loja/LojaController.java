package Up.Power.apresentacao.loja;

import Up.Power.aplicacao.loja.ItemLojaResumo;
import Up.Power.aplicacao.loja.LojaServicoAplicacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loja")
public class LojaController {

    private final LojaServicoAplicacao lojaServicoAplicacao;

    public LojaController(LojaServicoAplicacao lojaServicoAplicacao) {
        this.lojaServicoAplicacao = lojaServicoAplicacao;
    }

    @GetMapping("/itens")
    public ResponseEntity<List<ItemLojaResumo>> listarItens(
            @RequestParam(required = false) String categoria) {
        try {
            List<ItemLojaResumo> itens;
            if (categoria != null && !categoria.isEmpty()) {
                itens = lojaServicoAplicacao.listarItensPorCategoria(categoria);
            } else {
                itens = lojaServicoAplicacao.listarItens();
            }
            System.out.println("Total de itens retornados: " + (itens != null ? itens.size() : 0));
            return ResponseEntity.ok(itens);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao listar itens: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/itens/{id}")
    public ResponseEntity<ItemLojaResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            return lojaServicoAplicacao.obterPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/comprar")
    public ResponseEntity<?> comprarItem(@RequestBody ComprarItemRequest request) {
        try {
            lojaServicoAplicacao.comprarItem(request.avatarId(), request.acessorioId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao comprar item: " + e.getMessage()));
        }
    }

    // DTOs de Request
    public record ComprarItemRequest(Integer avatarId, Integer acessorioId) {}
    public record ErrorResponse(String message) {}
}


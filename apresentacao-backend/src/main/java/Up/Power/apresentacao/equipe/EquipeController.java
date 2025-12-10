package Up.Power.apresentacao.equipe;

import Up.Power.aplicacao.equipe.EquipeServicoAplicacao;
import Up.Power.aplicacao.equipe.EquipeResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeServicoAplicacao equipeServicoAplicacao;

    public EquipeController(EquipeServicoAplicacao equipeServicoAplicacao) {
        this.equipeServicoAplicacao = equipeServicoAplicacao;
    }

    @PostMapping
    public ResponseEntity<EquipeResumo> criarEquipe(@RequestBody CriarEquipeRequest request) {
        try {
            EquipeResumo equipe = equipeServicoAplicacao.criarEquipe(
                    request.id(),
                    request.nome(),
                    request.usuarioAdmEmail()
            );
            return ResponseEntity.ok(equipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            EquipeResumo equipe = equipeServicoAplicacao.obterPorId(id);
            if (equipe == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(equipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<EquipeResumo>> listarPorUsuario(@PathVariable("email") String email) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            List<EquipeResumo> equipes = equipeServicoAplicacao.listarPorUsuario(emailDecodificado);
            return ResponseEntity.ok(equipes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{equipeId}/membros")
    public ResponseEntity<EquipeResumo> adicionarMembro(
            @PathVariable("equipeId") Integer equipeId,
            @RequestBody AdicionarMembroRequest request) {
        try {
            EquipeResumo equipe = equipeServicoAplicacao.adicionarMembro(equipeId, request.novoMembroEmail());
            return ResponseEntity.ok(equipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{equipeId}/membros/{email}")
    public ResponseEntity<EquipeResumo> removerMembro(
            @PathVariable("equipeId") Integer equipeId,
            @PathVariable("email") String membroEmail) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(membroEmail, "UTF-8");
            EquipeResumo equipe = equipeServicoAplicacao.removerMembro(equipeId, emailDecodificado);
            return ResponseEntity.ok(equipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{equipeId}")
    public ResponseEntity<EquipeResumo> atualizarInformacoes(
            @PathVariable("equipeId") Integer equipeId,
            @RequestBody AtualizarInformacoesRequest request) {
        try {
            EquipeResumo equipe = equipeServicoAplicacao.atualizarInformacoes(
                    equipeId,
                    request.nome(),
                    request.descricao(),
                    request.foto()
            );
            return ResponseEntity.ok(equipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{equipeId}/periodo")
    public ResponseEntity<EquipeResumo> definirPeriodo(
            @PathVariable("equipeId") Integer equipeId,
            @RequestBody DefinirPeriodoRequest request) {
        try {
            EquipeResumo equipe = equipeServicoAplicacao.definirPeriodo(
                    equipeId,
                    request.inicio(),
                    request.fim()
            );
            return ResponseEntity.ok(equipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{equipeId}/is-lider/{email}")
    public ResponseEntity<Boolean> isLider(
            @PathVariable("equipeId") Integer equipeId,
            @PathVariable("email") String usuarioEmail) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(usuarioEmail, "UTF-8");
            boolean isLider = equipeServicoAplicacao.isLider(equipeId, emailDecodificado);
            return ResponseEntity.ok(isLider);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{equipeId}/is-membro/{email}")
    public ResponseEntity<Boolean> isMembro(
            @PathVariable("equipeId") Integer equipeId,
            @PathVariable("email") String usuarioEmail) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(usuarioEmail, "UTF-8");
            boolean isMembro = equipeServicoAplicacao.isMembro(equipeId, emailDecodificado);
            return ResponseEntity.ok(isMembro);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{equipeId}/membros")
    public ResponseEntity<List<String>> listarMembros(@PathVariable("equipeId") Integer equipeId) {
        try {
            List<String> membros = equipeServicoAplicacao.listarMembros(equipeId);
            return ResponseEntity.ok(membros);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DTOs de Request
    public record CriarEquipeRequest(Integer id, String nome, String usuarioAdmEmail) {}
    public record AdicionarMembroRequest(String novoMembroEmail) {}
    public record AtualizarInformacoesRequest(String nome, String descricao, String foto) {}
    public record DefinirPeriodoRequest(LocalDate inicio, LocalDate fim) {}
}


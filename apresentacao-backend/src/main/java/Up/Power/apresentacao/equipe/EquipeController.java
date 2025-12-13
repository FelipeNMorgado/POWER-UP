package Up.Power.apresentacao.equipe;

import Up.Power.aplicacao.equipe.EquipeServicoAplicacao;
import Up.Power.aplicacao.ranking.RankingEntrada;
import Up.Power.aplicacao.ranking.RankingServicoAplicacao;
import Up.Power.aplicacao.equipe.EquipeResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeServicoAplicacao equipeServicoAplicacao;
    private final RankingServicoAplicacao rankingServicoAplicacao;

    public EquipeController(
            EquipeServicoAplicacao equipeServicoAplicacao,
            RankingServicoAplicacao rankingServicoAplicacao
    ) {
        this.equipeServicoAplicacao = equipeServicoAplicacao;
        this.rankingServicoAplicacao = rankingServicoAplicacao;
    }

    @PostMapping
    public ResponseEntity<EquipeResumo> criarEquipe(@RequestBody CriarEquipeRequest request) {
        try {
            // Se o ID não foi fornecido, usar 0 para que o banco gere automaticamente
            Integer id = request.id() != null ? request.id() : 0;
            EquipeResumo equipe = equipeServicoAplicacao.criarEquipe(
                    id,
                    request.nome(),
                    request.usuarioAdmEmail(),
                    request.descricao()
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
            @PathVariable("email") String membroEmail,
            @RequestParam("usuarioEmail") String usuarioEmail) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(membroEmail, "UTF-8");
            String usuarioEmailDecodificado = java.net.URLDecoder.decode(usuarioEmail, "UTF-8");
            EquipeResumo equipe = equipeServicoAplicacao.removerMembro(equipeId, emailDecodificado, usuarioEmailDecodificado);
            return ResponseEntity.ok(equipe);
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
            LocalDate inicio = request.inicio() != null ? LocalDate.parse(request.inicio()) : null;
            LocalDate fim = request.fim() != null ? LocalDate.parse(request.fim()) : null;
            
            EquipeResumo equipe = equipeServicoAplicacao.definirPeriodo(
                    equipeId,
                    inicio,
                    fim
            );
            return ResponseEntity.ok(equipe);
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    @DeleteMapping("/{equipeId}")
    public ResponseEntity<Map<String, String>> excluirEquipe(
            @PathVariable("equipeId") Integer equipeId,
            @RequestParam("usuarioEmail") String usuarioEmail) {
        try {
            String usuarioEmailDecodificado = java.net.URLDecoder.decode(usuarioEmail, "UTF-8");
            equipeServicoAplicacao.excluirEquipe(equipeId, usuarioEmailDecodificado);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Equipe excluída com sucesso");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{equipeId}/ranking")
    public ResponseEntity<List<EquipeServicoAplicacao.MembroRanking>> obterRanking(@PathVariable("equipeId") Integer equipeId) {
        try {
            List<EquipeServicoAplicacao.MembroRanking> ranking = equipeServicoAplicacao.obterRanking(equipeId);
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DTOs de Request
    public record CriarEquipeRequest(Integer id, String nome, String usuarioAdmEmail, String descricao) {}
    public record AdicionarMembroRequest(String novoMembroEmail) {}
    public record AtualizarInformacoesRequest(String nome, String descricao, String foto) {}
    public record DefinirPeriodoRequest(String inicio, String fim) {}
}


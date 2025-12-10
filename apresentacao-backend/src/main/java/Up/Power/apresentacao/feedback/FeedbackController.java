package Up.Power.apresentacao.feedback;

import Up.Power.aplicacao.feedback.FeedbackServicoAplicacao;
import Up.Power.aplicacao.feedback.FeedbackResumo;
import Up.Power.feedback.Classificacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackServicoAplicacao feedbackServicoAplicacao;

    public FeedbackController(FeedbackServicoAplicacao feedbackServicoAplicacao) {
        this.feedbackServicoAplicacao = feedbackServicoAplicacao;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResumo> obter(@PathVariable("id") Integer id) {
        try {
            FeedbackResumo feedback = feedbackServicoAplicacao.obter(id);
            if (feedback == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable("email") String email) {
        System.out.println("========================================");
        System.out.println("[FEEDBACK_CONTROLLER] Listando feedbacks por usuário");
        System.out.println("[FEEDBACK_CONTROLLER] Email recebido (encoded): " + email);
        try {
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            System.out.println("[FEEDBACK_CONTROLLER] Email decodificado: " + emailDecodificado);
            System.out.println("[FEEDBACK_CONTROLLER] Chamando feedbackServicoAplicacao.listarPorUsuario...");
            List<FeedbackResumo> feedbacks = feedbackServicoAplicacao.listarPorUsuario(emailDecodificado);
            System.out.println("[FEEDBACK_CONTROLLER] Feedbacks encontrados: " + feedbacks.size());
            System.out.println("========================================");
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            System.err.println("[FEEDBACK_CONTROLLER] ========================================");
            System.err.println("[FEEDBACK_CONTROLLER] ERRO ao listar feedbacks:");
            System.err.println("[FEEDBACK_CONTROLLER] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_CONTROLLER] Mensagem: " + e.getMessage());
            System.err.println("[FEEDBACK_CONTROLLER] Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            e.printStackTrace();
            System.err.println("[FEEDBACK_CONTROLLER] ========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarFeedbackRequest request) {
        System.out.println("========================================");
        System.out.println("[FEEDBACK_CONTROLLER] Recebendo requisição para criar feedback");
        System.out.println("[FEEDBACK_CONTROLLER] Request: frequenciaId=" + request.frequenciaId() + 
                ", email=" + request.email() + 
                ", classificacao=" + request.classificacao() + 
                ", descricao=" + (request.descricao() != null ? request.descricao().substring(0, Math.min(50, request.descricao().length())) + "..." : "null"));
        try {
            if (request.frequenciaId() == null || request.email() == null || request.email().isEmpty()) {
                System.out.println("[FEEDBACK_CONTROLLER] ERRO: Campos obrigatórios inválidos");
                return ResponseEntity.badRequest().body("{\"erro\": \"Campos obrigatórios: frequenciaId e email\"}");
            }
            
            if (request.classificacao() == null) {
                System.out.println("[FEEDBACK_CONTROLLER] ERRO: Classificacao é obrigatória");
                return ResponseEntity.badRequest().body("{\"erro\": \"Classificacao é obrigatória\"}");
            }
            
            System.out.println("[FEEDBACK_CONTROLLER] Chamando feedbackServicoAplicacao.criar...");
            FeedbackResumo feedback = feedbackServicoAplicacao.criar(
                    request.frequenciaId(),
                    request.email(),
                    request.classificacao(),
                    request.descricao() != null ? request.descricao() : ""
            );
            System.out.println("[FEEDBACK_CONTROLLER] Feedback criado com sucesso. ID: " + feedback.id());
            System.out.println("========================================");
            return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
        } catch (Exception e) {
            System.err.println("[FEEDBACK_CONTROLLER] ========================================");
            System.err.println("[FEEDBACK_CONTROLLER] ERRO ao criar feedback:");
            System.err.println("[FEEDBACK_CONTROLLER] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_CONTROLLER] Mensagem: " + e.getMessage());
            System.err.println("[FEEDBACK_CONTROLLER] Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            e.printStackTrace();
            System.err.println("[FEEDBACK_CONTROLLER] ========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResumo> modificar(
            @PathVariable("id") Integer id,
            @RequestBody ModificarFeedbackRequest request) {
        try {
            FeedbackResumo feedback = feedbackServicoAplicacao.modificar(
                    id,
                    request.classificacao(),
                    request.descricao() != null ? request.descricao() : ""
            );
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Integer id) {
        try {
            feedbackServicoAplicacao.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public record CriarFeedbackRequest(
            Integer frequenciaId,
            String email,
            Classificacao classificacao,
            String descricao
    ) {}

    public record ModificarFeedbackRequest(
            Classificacao classificacao,
            String descricao
    ) {}
}


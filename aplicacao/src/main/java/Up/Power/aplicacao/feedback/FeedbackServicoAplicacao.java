package Up.Power.aplicacao.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.Classificacao;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackService;
import Up.Power.frequencia.FrequenciaId;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServicoAplicacao {

    private final FeedbackRepositorioAplicacao repo;
    private final FeedbackService feedbackService;

    public FeedbackServicoAplicacao(FeedbackRepositorioAplicacao repo, FeedbackService feedbackService) {
        this.repo = repo;
        this.feedbackService = feedbackService;
    }

    public FeedbackResumo obter(Integer id) {
        var feedback = repo.obter(id);
        return feedback == null ? null : toResumo(feedback);
    }

    public List<FeedbackResumo> listarPorUsuario(String email) {
        System.out.println("[FEEDBACK_SERVICE] Listando feedbacks para usuário: " + email);
        try {
            List<FeedbackResumo> feedbacks = repo.listarPorUsuario(email);
            System.out.println("[FEEDBACK_SERVICE] Feedbacks encontrados: " + feedbacks.size());
            return feedbacks;
        } catch (Exception e) {
            System.err.println("[FEEDBACK_SERVICE] ERRO ao listar feedbacks:");
            System.err.println("[FEEDBACK_SERVICE] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_SERVICE] Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public FeedbackResumo criar(Integer frequenciaId, String email, Classificacao classificacao, String descricao) {
        System.out.println("[FEEDBACK_SERVICE] ========================================");
        System.out.println("[FEEDBACK_SERVICE] Criando feedback");
        System.out.println("[FEEDBACK_SERVICE] frequenciaId: " + frequenciaId);
        System.out.println("[FEEDBACK_SERVICE] email: " + email);
        System.out.println("[FEEDBACK_SERVICE] classificacao: " + classificacao);
        System.out.println("[FEEDBACK_SERVICE] descricao: " + (descricao != null ? descricao.substring(0, Math.min(50, descricao.length())) + "..." : "null"));
        
        Date dataAtual = new Date();
        try {
            // Criar feedback diretamente sem validação de frequência
            // Permitir múltiplos feedbacks para a mesma frequência
            System.out.println("[FEEDBACK_SERVICE] Criando feedback diretamente...");
            Feedback feedback = new Feedback(
                    new FeedbackId(0),
                    new FrequenciaId(frequenciaId),
                    classificacao,
                    descricao != null ? descricao : "",
                    new Email(email),
                    dataAtual
            );
            System.out.println("[FEEDBACK_SERVICE] Feedback criado. Chamando repo.criar...");
            Feedback salvo = repo.criar(feedback);
            System.out.println("[FEEDBACK_SERVICE] Feedback salvo com sucesso. ID: " + 
                    (salvo.getId() != null ? salvo.getId().getId() : "null"));
            FeedbackResumo resumo = toResumo(salvo);
            System.out.println("[FEEDBACK_SERVICE] Resumo criado. ID: " + resumo.id());
            System.out.println("[FEEDBACK_SERVICE] ========================================");
            return resumo;
        } catch (Exception e) {
            System.err.println("[FEEDBACK_SERVICE] ========================================");
            System.err.println("[FEEDBACK_SERVICE] ERRO ao criar feedback:");
            System.err.println("[FEEDBACK_SERVICE] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_SERVICE] Mensagem: " + e.getMessage());
            System.err.println("[FEEDBACK_SERVICE] Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            e.printStackTrace();
            System.err.println("[FEEDBACK_SERVICE] ========================================");
            throw e;
        }
    }

    public FeedbackResumo modificar(Integer id, Classificacao classificacao, String descricao) {
        Feedback feedback = feedbackService.editarFeedback(
                new FeedbackId(id),
                descricao,
                classificacao
        );
        return toResumo(feedback);
    }

    public void excluir(Integer id) {
        feedbackService.excluirFeedback(new FeedbackId(id));
    }

    private FeedbackResumo toResumo(Feedback f) {
        return new FeedbackResumo(
                f.getId().getId(),
                f.getFrequencia().getId(),
                f.getClassificacao(),
                f.getFeedback(),
                f.getEmail().getCaracteres(),
                f.getData()
        );
    }
}

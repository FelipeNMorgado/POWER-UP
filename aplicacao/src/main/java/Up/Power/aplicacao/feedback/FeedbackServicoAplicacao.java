package Up.Power.aplicacao.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.Classificacao;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackService;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.frequencia.FrequenciaRepository;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.aplicacao.planoTreino.PlanoTreinoRepositorioAplicacao;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServicoAplicacao {

    private final FeedbackRepositorioAplicacao repo;
    private final FeedbackService feedbackService;
    private final FrequenciaRepository frequenciaRepository;
    private final PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao;

    public FeedbackServicoAplicacao(
            FeedbackRepositorioAplicacao repo, 
            FeedbackService feedbackService,
            FrequenciaRepository frequenciaRepository,
            PlanoTreinoRepositorioAplicacao planoTreinoRepositorioAplicacao) {
        this.repo = repo;
        this.feedbackService = feedbackService;
        this.frequenciaRepository = frequenciaRepository;
        this.planoTreinoRepositorioAplicacao = planoTreinoRepositorioAplicacao;
    }

    public FeedbackResumo obter(Integer id) {
        var feedback = repo.obter(id);
        return feedback == null ? null : toResumo(feedback);
    }

    public List<FeedbackResumo> listarPorUsuario(String email) {
        System.out.println("[FEEDBACK_SERVICE] Listando feedbacks para usuário: " + email);
        try {
            // Buscar feedbacks do repositório (que retorna FeedbackResumo sem nome do plano)
            List<FeedbackResumo> feedbacksSemNome = repo.listarPorUsuario(email);
            
            // Enriquecer cada feedback com o nome do plano de treino
            List<FeedbackResumo> feedbacks = feedbacksSemNome.stream()
                .map(f -> {
                    try {
                        // Buscar o feedback do domínio para obter a frequência
                        Feedback feedbackDominio = repo.obter(f.id());
                        if (feedbackDominio != null) {
                            return toResumo(feedbackDominio);
                        }
                        // Se não conseguir buscar, retornar com nome null
                        return new FeedbackResumo(
                            f.id(), f.frequencia(), f.classificacao(), f.feedback(), 
                            f.email(), f.data(), null
                        );
                    } catch (Exception e) {
                        System.err.println("Erro ao enriquecer feedback " + f.id() + ": " + e.getMessage());
                        return new FeedbackResumo(
                            f.id(), f.frequencia(), f.classificacao(), f.feedback(), 
                            f.email(), f.data(), null
                        );
                    }
                })
                .collect(java.util.stream.Collectors.toList());
            
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
        // Buscar nome do plano de treino através da frequência
        String nomePlanoTreino = null;
        try {
            var frequencia = frequenciaRepository.obterFrequencia(f.getFrequencia(), null);
            if (frequencia != null && frequencia.getPlanoTId() != null) {
                Optional<Up.Power.PlanoTreino> planoOpt = planoTreinoRepositorioAplicacao.obterPorId(frequencia.getPlanoTId());
                if (planoOpt.isPresent()) {
                    nomePlanoTreino = planoOpt.get().getNome();
                } else {
                    nomePlanoTreino = "Plano deletado";
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar nome do plano de treino para feedback: " + e.getMessage());
            nomePlanoTreino = "Plano não encontrado";
        }
        
        return new FeedbackResumo(
                f.getId().getId(),
                f.getFrequencia().getId(),
                f.getClassificacao(),
                f.getFeedback(),
                f.getEmail().getCaracteres(),
                f.getData(),
                nomePlanoTreino
        );
    }
}

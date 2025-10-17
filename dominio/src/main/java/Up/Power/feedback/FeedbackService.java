package Up.Power.feedback;

import Up.Power.Email;
import Up.Power.Feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackRepository;
import Up.Power.feedback.Classificacao;
import Up.Power.frequencia.FrequenciaId;

import java.util.UUID;

public class FeedbackService {

        private final FeedbackRepository repository;

        public FeedbackService(FeedbackRepository repository) {
            this.repository = repository;
        }

        // Criar um novo feedback
        public Feedback adicionarFeedback(FeedbackId id, FrequenciaId frequencia, Email email, String descricao, Classificacao classificacao) {
            Feedback feedback = new Feedback(id, frequencia, classificacao, descricao, email);
            repository.salvar(feedback);
            return feedback;
        }

        // Editar um feedback existente
        public Feedback editarFeedback(FeedbackId id, String novaDescricao, Classificacao novaClassificacao) {
            Feedback existente = repository.obter(id);
            if (existente == null) {
                throw new IllegalArgumentException("Feedback não encontrado para edição: " + id);
            }

            existente.setFeedback(novaDescricao);
            existente.setClassificacao(novaClassificacao);
            repository.salvar(existente);
            return existente;
        }

        public void excluirFeedback(FeedbackId id) {
            repository.deletar(id);
        }

        public Feedback obterFeedback(FeedbackId id) {
            return repository.obter(id);
        }
}
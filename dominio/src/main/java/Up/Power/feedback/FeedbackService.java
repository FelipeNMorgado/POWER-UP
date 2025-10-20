package Up.Power.feedback;

import Up.Power.*;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackRepository;
import Up.Power.feedback.Classificacao;
import Up.Power.frequencia.FrequenciaId;

import java.util.Date;
import java.util.UUID;

public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public Feedback adicionarFeedback(FeedbackId id, FrequenciaId frequencia, Email email, String descricao, Classificacao classificacao, Date dataAtual) {
        if (feedbackJaExiste(dataAtual)) {
            throw new IllegalStateException("Já existe um feedback cadastrado para essa data.");
        }

        Feedback feedback = new Feedback(id, frequencia, classificacao, descricao, email, dataAtual);
        repository.salvar(feedback);
        return feedback;
    }


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

        public boolean feedbackJaExiste(Date dataAtual){
            if (repository.obterPorData(dataAtual) != null) {
                return true;
            }
            return false;
        }
}
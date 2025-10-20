package Up.Power;

import Up.Power.feedback.Classificacao;
import Up.Power.feedback.FeedbackId;
import Up.Power.frequencia.FrequenciaId;

import java.util.Date;

public class Feedback {
    private FeedbackId id;
    private FrequenciaId frequencia;
    private Classificacao classificacao;
    private String feedback;
    private Email usuarioEmail;
    private Date data;

    public Feedback(FeedbackId id, FrequenciaId frequencia, Classificacao classificacao, String feedback, Email usuarioEmail, Date dataAtual) {
        this.id = id;
        this.frequencia = frequencia;
        this.classificacao = classificacao;
        this.feedback = feedback;
        this.usuarioEmail = usuarioEmail;
        this.data = dataAtual;
    }

    public Email getEmail() { return usuarioEmail; }
    public FeedbackId getId() { return id; }
    public FrequenciaId getFrequencia() { return frequencia; }
    public Classificacao getClassificacao() { return classificacao; }
    public String getFeedback() { return feedback; }
    public Date getData() { return data; }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}



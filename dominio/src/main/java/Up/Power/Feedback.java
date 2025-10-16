package Up.Power;

import Up.Power.feedback.FeedbackId;
import Up.Power.frequencia.FrequenciaId;

public class Feedback {
    private FeedbackId id;
    private FrequenciaId frequencia;
    private Classificacao classificacao;
    private String feedback;

    public Feedback(FeedbackId id, FrequenciaId frequencia, Classificacao classificacao, String feedback) {
        this.id = id;
        this.frequencia = frequencia;
        this.classificacao = classificacao;
        this.feedback = feedback;
    }

    public FeedbackId getId() { return id; }
    public FrequenciaId getFrequencia() { return frequencia; }
    public Classificacao getClassificacao() { return classificacao; }
    public String getFeedback() { return feedback; }
}



package Up.Power.infraestrutura.persistencia.jpa.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import Up.Power.frequencia.FrequenciaId;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public FeedbackJpa toEntity(Feedback domain) {
        FeedbackJpa entity = new FeedbackJpa();

        if (domain.getId() != null)
            entity.setId(domain.getId().getId());

        if (domain.getFrequencia() != null)
            entity.setFrequencia(domain.getFrequencia().getId());

        entity.setClassificacao(domain.getClassificacao());
        entity.setFeedback(domain.getFeedback());
        entity.setEmail(domain.getEmail().getCaracteres());
        entity.setData(domain.getData());

        return entity;
    }

    public static Feedback toDomain(FeedbackJpa entity) {
        Feedback domain = new Feedback(
                new FeedbackId(entity.getId()),
                new FrequenciaId(entity.getFrequencia()),
                entity.getClassificacao(),
                entity.getFeedback(),
                new Email(entity.getEmail()),
                entity.getData()
        );

        if (entity.getClassificacao() != null)
            domain.setClassificacao(entity.getClassificacao());

        if (entity.getFeedback() != null)
            domain.setFeedback(entity.getFeedback());

        return domain;
    }
}

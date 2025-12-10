package Up.Power.infraestrutura.persistencia.jpa.feedback;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import Up.Power.frequencia.FrequenciaId;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public FeedbackJpa toEntity(Feedback domain) {
        System.out.println("[FEEDBACK_MAPPER] Convertendo Feedback para FeedbackJpa");
        System.out.println("[FEEDBACK_MAPPER] Domain ID: " + (domain.getId() != null ? domain.getId().getId() : "null"));
        System.out.println("[FEEDBACK_MAPPER] Domain Frequencia: " + (domain.getFrequencia() != null ? domain.getFrequencia().getId() : "null"));
        System.out.println("[FEEDBACK_MAPPER] Domain Classificacao: " + domain.getClassificacao());
        System.out.println("[FEEDBACK_MAPPER] Domain Email: " + (domain.getEmail() != null ? domain.getEmail().getCaracteres() : "null"));
        
        FeedbackJpa entity = new FeedbackJpa();

        // Se o ID for 0 ou null, usar null para que o JPA gere o ID automaticamente (INSERT)
        // Se o ID for > 0, usar o ID para UPDATE
        Integer id = (domain.getId() == null || domain.getId().getId() == 0) 
            ? null 
            : domain.getId().getId();
        entity.setId(id);
        System.out.println("[FEEDBACK_MAPPER] Entity ID definido: " + entity.getId());

        if (domain.getFrequencia() != null) {
            entity.setFrequencia(domain.getFrequencia().getId());
            System.out.println("[FEEDBACK_MAPPER] Entity Frequencia definida: " + entity.getFrequencia());
        } else {
            System.err.println("[FEEDBACK_MAPPER] ERRO: Frequencia é null!");
        }

        entity.setClassificacao(domain.getClassificacao());
        entity.setFeedback(domain.getFeedback());
        
        if (domain.getEmail() != null) {
            entity.setEmail(domain.getEmail().getCaracteres());
            System.out.println("[FEEDBACK_MAPPER] Entity Email definido: " + entity.getEmail());
        } else {
            System.err.println("[FEEDBACK_MAPPER] ERRO: Email é null!");
        }
        
        entity.setData(domain.getData());
        System.out.println("[FEEDBACK_MAPPER] Entity criada com sucesso");

        return entity;
    }

    public static Feedback toDomain(FeedbackJpa entity) {
        System.out.println("[FEEDBACK_MAPPER] Convertendo FeedbackJpa para Feedback");
        System.out.println("[FEEDBACK_MAPPER] Entity ID: " + entity.getId());
        System.out.println("[FEEDBACK_MAPPER] Entity Frequencia: " + entity.getFrequencia());
        System.out.println("[FEEDBACK_MAPPER] Entity Classificacao: " + entity.getClassificacao());
        System.out.println("[FEEDBACK_MAPPER] Entity Email: " + entity.getEmail());
        System.out.println("[FEEDBACK_MAPPER] Entity Data: " + entity.getData());
        
        try {
            if (entity == null) {
                System.err.println("[FEEDBACK_MAPPER] ERRO: Entity é null!");
                throw new IllegalArgumentException("Entity não pode ser null");
            }
            
            if (entity.getId() == null) {
                System.err.println("[FEEDBACK_MAPPER] ERRO: Entity ID é null!");
                throw new IllegalArgumentException("Entity ID não pode ser null");
            }
            
            if (entity.getFrequencia() == null) {
                System.err.println("[FEEDBACK_MAPPER] ERRO: Entity Frequencia é null!");
                throw new IllegalArgumentException("Entity Frequencia não pode ser null");
            }
            
            if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
                System.err.println("[FEEDBACK_MAPPER] ERRO: Entity Email é null ou vazio!");
                throw new IllegalArgumentException("Entity Email não pode ser null ou vazio");
            }
            
            if (entity.getClassificacao() == null) {
                System.err.println("[FEEDBACK_MAPPER] ERRO: Entity Classificacao é null!");
                throw new IllegalArgumentException("Entity Classificacao não pode ser null");
            }
            
            Feedback domain = new Feedback(
                    new FeedbackId(entity.getId()),
                    new FrequenciaId(entity.getFrequencia()),
                    entity.getClassificacao(),
                    entity.getFeedback() != null ? entity.getFeedback() : "",
                    new Email(entity.getEmail()),
                    entity.getData() != null ? entity.getData() : new java.util.Date()
            );

            System.out.println("[FEEDBACK_MAPPER] Domain criado com sucesso");
            return domain;
        } catch (Exception e) {
            System.err.println("[FEEDBACK_MAPPER] ERRO ao converter para domain:");
            System.err.println("[FEEDBACK_MAPPER] Tipo: " + e.getClass().getName());
            System.err.println("[FEEDBACK_MAPPER] Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

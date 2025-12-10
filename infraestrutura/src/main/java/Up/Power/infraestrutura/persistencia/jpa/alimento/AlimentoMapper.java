package Up.Power.infraestrutura.persistencia.jpa.alimento;

import Up.Power.Alimento;
import Up.Power.alimento.AlimentoId;
import org.springframework.stereotype.Component;

@Component
public class AlimentoMapper {

    public AlimentoJpa toEntity(Alimento alimento) {
        return new AlimentoJpa(
                alimento.getAlimento() != null ? alimento.getAlimento().getId() : null,
                alimento.getCategoria(),
                alimento.getNome(),
                alimento.getCalorias(),
                alimento.getGramas()
        );
    }

    public Alimento toDomain(AlimentoJpa entity) {
        System.out.println("[ALIMENTO_MAPPER] ========================================");
        System.out.println("[ALIMENTO_MAPPER] Convertendo JPA para Domain");
        System.out.println("[ALIMENTO_MAPPER] ID: " + entity.getId());
        System.out.println("[ALIMENTO_MAPPER] Nome: " + entity.getNome());
        
        // Verificar categoria antes de usar
        if (entity.getCategoria() == null) {
            System.err.println("[ALIMENTO_MAPPER] ERRO: Categoria é null para alimento ID: " + entity.getId());
            throw new IllegalStateException("Categoria não pode ser null para alimento ID: " + entity.getId());
        }
        
        System.out.println("[ALIMENTO_MAPPER] Categoria (enum): " + entity.getCategoria().name());
        System.out.println("[ALIMENTO_MAPPER] Calorias: " + entity.getCalorias());
        System.out.println("[ALIMENTO_MAPPER] Gramas: " + entity.getGramas());
        
        try {
            Alimento domain = new Alimento(
                    new AlimentoId(entity.getId()),
                    entity.getCategoria(),
                    entity.getNome(),
                    entity.getCalorias(),
                    entity.getGramas() != null ? entity.getGramas() : 0.0f
            );
            System.out.println("[ALIMENTO_MAPPER] Domain criado com sucesso");
            System.out.println("[ALIMENTO_MAPPER] ========================================");
            return domain;
        } catch (Exception e) {
            System.err.println("[ALIMENTO_MAPPER] ERRO ao criar domain:");
            System.err.println("[ALIMENTO_MAPPER] Tipo: " + e.getClass().getName());
            System.err.println("[ALIMENTO_MAPPER] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[ALIMENTO_MAPPER] Causa: " + (e.getCause() != null ? e.getCause().getClass().getName() + " - " + e.getCause().getMessage() : "null"));
            e.printStackTrace();
            System.err.println("[ALIMENTO_MAPPER] ========================================");
            throw e;
        }
    }
}


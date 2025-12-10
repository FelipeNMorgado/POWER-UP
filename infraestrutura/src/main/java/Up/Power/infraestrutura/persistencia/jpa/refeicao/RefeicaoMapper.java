package Up.Power.infraestrutura.persistencia.jpa.refeicao;

import Up.Power.Refeicao;
import Up.Power.alimento.AlimentoId;
import Up.Power.refeicao.RefeicaoId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RefeicaoMapper {

    public RefeicaoJpa toEntity(Refeicao refeicao) {
        System.out.println("[REFEICAO_MAPPER] ========================================");
        System.out.println("[REFEICAO_MAPPER] Convertendo Domain para JPA");
        System.out.println("[REFEICAO_MAPPER] ID do domain: " + 
                (refeicao.getId() != null ? refeicao.getId().getId() : "null"));
        System.out.println("[REFEICAO_MAPPER] Tipo: " + refeicao.getTipo());
        System.out.println("[REFEICAO_MAPPER] Alimentos: " + 
                (refeicao.getAlimentos() != null ? refeicao.getAlimentos().size() + " itens" : "null"));
        System.out.println("[REFEICAO_MAPPER] Calorias totais: " + refeicao.getCaloriasTotais());
        System.out.println("[REFEICAO_MAPPER] Inicio: " + refeicao.getInicio() + ", Fim: " + refeicao.getFim());
        
        List<Integer> alimentosIds = refeicao.getAlimentos() != null 
                ? refeicao.getAlimentos().stream()
                        .map(AlimentoId::getId)
                        .collect(Collectors.toList())
                : java.util.Collections.emptyList();
        
        System.out.println("[REFEICAO_MAPPER] Alimentos IDs extraídos: " + alimentosIds);

        // Se o ID for 0 ou null, usar null para que o JPA gere o ID automaticamente (INSERT)
        // Se o ID for > 0, usar o ID para UPDATE
        Integer id = (refeicao.getId() == null || refeicao.getId().getId() == 0) 
            ? null 
            : refeicao.getId().getId();
        System.out.println("[REFEICAO_MAPPER] ID para JPA: " + id + " (null = INSERT, não-null = UPDATE)");
        
        System.out.println("[REFEICAO_MAPPER] Criando entidade JPA...");
        RefeicaoJpa entity = new RefeicaoJpa(
                id,
                refeicao.getTipo(),
                alimentosIds,
                refeicao.getCaloriasTotais(),
                refeicao.getInicio(),
                refeicao.getFim()
        );
        
        System.out.println("[REFEICAO_MAPPER] Entity criada. ID após criação: " + entity.getId());
        System.out.println("[REFEICAO_MAPPER] ========================================");
        return entity;
    }

    public Refeicao toDomain(RefeicaoJpa entity) {
        Refeicao refeicao = new Refeicao(
                new RefeicaoId(entity.getId()),
                entity.getTipo()
        );

        if (entity.getAlimentosIds() != null) {
            for (Integer alimentoId : entity.getAlimentosIds()) {
                refeicao.adicionarAlimento(new AlimentoId(alimentoId));
            }
        }

        refeicao.setCaloriasTotais(entity.getCaloriasTotais());
        refeicao.setInicio(entity.getInicio());
        refeicao.setFim(entity.getFim());

        return refeicao;
    }
}


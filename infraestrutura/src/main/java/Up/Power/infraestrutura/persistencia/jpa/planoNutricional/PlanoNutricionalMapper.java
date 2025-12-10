package Up.Power.infraestrutura.persistencia.jpa.planoNutricional;

import Up.Power.PlanoNutricional;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.planoNutricional.Objetivo;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.EstadoPlano;
import Up.Power.Email;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanoNutricionalMapper {

    public PlanoNutricional toDomain(PlanoNutricionalJpa jpa) {
        System.out.println("[MAPPER] Convertendo JPA para Domain. Plano ID=" + jpa.getId() + 
                ", Objetivo=" + jpa.getObjetivo() + 
                ", Refeições JPA: " + (jpa.getRefeicoes() != null ? jpa.getRefeicoes().size() + " itens: " + jpa.getRefeicoes() : "null"));
        
        PlanoNutricional dominio =
                new PlanoNutricional(
                        new PlanoNId(jpa.getId()),
                        jpa.getObjetivo(),
                        new Email(jpa.getUsuarioEmail())
                );

        // IMPORTANTE: Sempre criar uma nova lista para evitar compartilhamento de referência
        // O JPA pode compartilhar a mesma lista entre diferentes entidades se não criarmos uma cópia
        if (jpa.getRefeicoes() != null && !jpa.getRefeicoes().isEmpty()) {
            // Criar uma cópia da lista para evitar compartilhamento de referência
            List<Integer> refeicoesIdsJpa = new ArrayList<>(jpa.getRefeicoes());
            List<RefeicaoId> refeicoesIds = refeicoesIdsJpa.stream()
                    .map(RefeicaoId::new)
                    .collect(Collectors.toList());
            System.out.println("[MAPPER] Adicionando " + refeicoesIds.size() + " refeições ao domínio: " + 
                    refeicoesIds.stream().map(r -> r.getId()).collect(Collectors.toList()));
            dominio.adicionarRefeicaoList(refeicoesIds);
        } else {
            System.out.println("[MAPPER] Nenhuma refeição para adicionar (lista vazia ou null)");
        }

        dominio.definirCaloriasObjetivo(jpa.getCaloriasObjetivo());
        dominio.definirCaloriasTotais(jpa.getCaloriasTotais());

        System.out.println("[MAPPER] Domínio criado. Refeições no domínio: " + 
                (dominio.getRefeicoes() != null ? dominio.getRefeicoes().size() : 0));
        return dominio;
    }

    public PlanoNutricionalJpa toEntity(PlanoNutricional plano) {
        System.out.println("[MAPPER] Convertendo PlanoNutricional para PlanoNutricionalJpa...");
        if (plano == null) {
            System.out.println("[MAPPER] ERRO: PlanoNutricional é null!");
            return null;
        }

        System.out.println("[MAPPER] Dados do plano:");
        System.out.println("[MAPPER]   - id: " + (plano.getId() != null ? plano.getId().getId() : "null"));
        System.out.println("[MAPPER]   - objetivo: " + (plano.getObjetivo() != null ? plano.getObjetivo().name() : "null"));
        System.out.println("[MAPPER]   - email: " + (plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : "null"));
        System.out.println("[MAPPER]   - caloriasTotais: " + plano.getCaloriasTotais());
        System.out.println("[MAPPER]   - caloriasObjetivo: " + plano.getCaloriasObjetivo());
        System.out.println("[MAPPER]   - refeicoes: " + (plano.getRefeicoes() != null ? plano.getRefeicoes().size() + " itens" : "null"));

        List<Integer> refeicoesIds = plano.getRefeicoes() != null 
            ? plano.getRefeicoes().stream()
                    .map(RefeicaoId::getId)
                    .collect(Collectors.toList())
            : new ArrayList<>();
        System.out.println("[MAPPER] Refeições convertidas: " + refeicoesIds.size() + " IDs");
        if (!refeicoesIds.isEmpty()) {
            System.out.println("[MAPPER] IDs das refeições: " + refeicoesIds);
        }

        // Se o ID for 0 ou null, usar null para que o JPA gere o ID automaticamente (INSERT)
        // Se o ID for > 0, usar o ID para UPDATE
        Integer id = (plano.getId() == null || plano.getId().getId() == 0) 
            ? null 
            : plano.getId().getId();
        System.out.println("[MAPPER] ID para JPA: " + id + " (null = INSERT, não-null = UPDATE)");

        System.out.println("[MAPPER] Criando entidade JPA...");
        // IMPORTANTE: Garantir que a lista de refeições seja uma nova instância
        // para evitar problemas de compartilhamento de referência
        List<Integer> refeicoesIdsCopy = refeicoesIds != null 
            ? new ArrayList<>(refeicoesIds) 
            : new ArrayList<>();
        
        System.out.println("[MAPPER] Criando entidade com " + refeicoesIdsCopy.size() + " refeições: " + refeicoesIdsCopy);
        
        PlanoNutricionalJpa entity = new PlanoNutricionalJpa(
                id,
                plano.getObjetivo(),
                EstadoPlano.Ativo,
                refeicoesIdsCopy,
                plano.getCaloriasTotais(),
                plano.getCaloriasObjetivo(),
                plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : null
        );
        
        // Verificar se a entidade foi criada corretamente
        System.out.println("[MAPPER] Entidade JPA criada. Refeições na entidade: " + 
                (entity.getRefeicoes() != null ? entity.getRefeicoes().size() + " itens: " + entity.getRefeicoes() : "null"));
        
        return entity;
    }
}

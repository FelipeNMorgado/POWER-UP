package Up.Power.infraestrutura.persistencia.jpa.loja;

import Up.Power.Acessorio;
import Up.Power.Loja;
import Up.Power.loja.LojaId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LojaMapper {

    public LojaJpa toEntity(Loja loja) {
        List<Integer> acessoriosIds = loja.getAcessorios().stream()
                .map(acessorio -> acessorio.getId().getId())
                .collect(Collectors.toList());

        return new LojaJpa(
                loja.getId() != null ? loja.getId().getId() : null,
                acessoriosIds
        );
    }

    public Loja toDomain(LojaJpa entity) {
        Loja loja = new Loja(new LojaId(entity.getId()));

        // Nota: Para converter de volta, precisaríamos do AcessorioRepository
        // Por enquanto, deixamos a lista vazia ou precisamos injetar o repositório
        // Isso pode ser ajustado quando necessário

        return loja;
    }
}


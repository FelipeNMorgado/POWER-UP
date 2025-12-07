package Up.Power.infraestrutura.persistencia.jpa.acessorio;

import Up.Power.Acessorio;
import Up.Power.acessorio.AcessorioId;
import org.springframework.stereotype.Component;

@Component
public class AcessorioMapper {

    public AcessorioJpa toEntity(Acessorio acessorio) {
        return new AcessorioJpa(
                acessorio.getId() != null ? acessorio.getId().getId() : null,
                acessorio.getIcone(),
                acessorio.getPreco(),
                acessorio.getNome(),
                acessorio.getImagem()
        );
    }

    public Acessorio toDomain(AcessorioJpa entity) {
        return new Acessorio(
                new AcessorioId(entity.getId()),
                entity.getIcone(),
                entity.getPreco(),
                entity.getNome(),
                entity.getImagem()
        );
    }
}


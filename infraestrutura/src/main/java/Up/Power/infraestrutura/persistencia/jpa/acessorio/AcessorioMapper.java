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
                acessorio.getImagem(),
                acessorio.getQualidade(),
                acessorio.getCategoria(),
                acessorio.getSubcategoria()
        );
    }

    public Acessorio toDomain(AcessorioJpa entity) {
        return new Acessorio(
                new AcessorioId(entity.getId()),
                entity.getIcone() != null ? entity.getIcone() : "",
                entity.getPreco() != null ? entity.getPreco() : 0,
                entity.getNome() != null ? entity.getNome() : "",
                entity.getImagem() != null ? entity.getImagem() : "",
                entity.getQualidade() != null ? entity.getQualidade() : "Basica",
                entity.getCategoria() != null ? entity.getCategoria() : "Acessorio",
                entity.getSubcategoria()
        );
    }
}


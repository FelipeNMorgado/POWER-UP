package Up.Power.infraestrutura.persistencia.jpa.equipe;

import Up.Power.Email;
import Up.Power.Equipe;
import Up.Power.equipe.EquipeId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EquipeMapper {

    // DOMAIN → ENTITY
    public EquipeJpa toEntity(Equipe equipe) {
        List<String> usuariosEmails = equipe.getUsuariosEmails().stream()
                .map(Email::getCaracteres)
                .collect(Collectors.toList());

        EquipeJpa entity = new EquipeJpa();
        
        // Só define ID se não for 0 (nova equipe)
        if (equipe.getId() != null && equipe.getId().getId() != 0) {
            entity.setId(equipe.getId().getId());
        }
        
        entity.setNome(equipe.getNome());
        entity.setDescricao(equipe.getDescricao());
        entity.setFoto(equipe.getFoto());
        entity.setInicio(equipe.getInicio());
        entity.setFim(equipe.getFim());
        entity.setUsuarioAdm(equipe.getUsuarioAdm().getCaracteres());
        entity.setUsuariosEmails(usuariosEmails);

        return entity;
    }

    // ENTITY → DOMAIN
    public Equipe toDomain(EquipeJpa entity) {
        Equipe equipe = new Equipe(
                new EquipeId(entity.getId()),
                entity.getNome(),
                new Email(entity.getUsuarioAdm())
        );

        // Configurar descrição e foto
        equipe.setDescricao(entity.getDescricao());
        equipe.setFoto(entity.getFoto());

        // Configurar período (permite definir cada data independentemente)
        if (entity.getInicio() != null) {
            equipe.setInicio(entity.getInicio());
        }
        if (entity.getFim() != null) {
            equipe.setFim(entity.getFim());
        }

        // Adicionar membros (exceto o admin que já foi adicionado no construtor)
        if (entity.getUsuariosEmails() != null) {
            for (String emailStr : entity.getUsuariosEmails()) {
                Email email = new Email(emailStr);
                // Não adiciona o admin novamente, pois já está no construtor
                if (!email.equals(equipe.getUsuarioAdm())) {
                    equipe.adicionarMembro(email);
                }
            }
        }

        return equipe;
    }
}


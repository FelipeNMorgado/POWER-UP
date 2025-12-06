package Up.Power.infraestrutura.persistencia.jpa.perfil;

import Up.Power.Email;
import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;

public class PerfilMapper {

    public static PerfilJpa toEntity(Perfil perfil) {
        return new PerfilJpa(
                perfil.getId() == null ? null : perfil.getId().getId(),
                perfil.getUsuarioEmail().getCaracteres(),
                perfil.getUsername(),
                perfil.isEstado(),
                perfil.getCriacao(),
                perfil.getFoto()
        );
    }

    public static Perfil toDomain(PerfilJpa entity) {
        // Nota: As listas (planosTreinos, conquistas, metas, amigos) são inicializadas vazias
        // Elas podem ser carregadas via queries específicas quando necessário
        Perfil perfil = new Perfil(
                new PerfilId(entity.getId()),
                new Email(entity.getUsuarioEmail()),
                entity.getUsername()
        );
        
        perfil.setEstado(entity.getEstado());
        perfil.setFoto(entity.getFoto());
        // criacao já é definida no construtor como LocalDateTime.now()
        // Se precisar manter a data original do banco, seria necessário adicionar um setter

        return perfil;
    }
}


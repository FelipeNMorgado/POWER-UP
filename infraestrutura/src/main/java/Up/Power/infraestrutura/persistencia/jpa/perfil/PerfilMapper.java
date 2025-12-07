package Up.Power.infraestrutura.persistencia.jpa.perfil;

import Up.Power.Email;
import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.infraestrutura.persistencia.jpa.planoTreino.PlanoTreinoJpa;
import Up.Power.infraestrutura.persistencia.jpa.consquista.ConquistaJpa;
import Up.Power.infraestrutura.persistencia.jpa.meta.MetaJpa;
import Up.Power.infraestrutura.persistencia.jpa.consquista.ConquistaMapper;
import Up.Power.infraestrutura.persistencia.jpa.meta.MetaMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PerfilMapper {

    private final MetaMapper metaMapper;

    public PerfilMapper(MetaMapper metaMapper) {
        this.metaMapper = metaMapper;
    }

    public PerfilJpa toEntity(Perfil perfil, 
                             List<PlanoTreinoJpa> planosTreinosJpa,
                             List<ConquistaJpa> conquistasJpa,
                             List<MetaJpa> metasJpa,
                             List<PerfilJpa> amigosJpa) {
        return new PerfilJpa(
                perfil.getId() == null ? null : perfil.getId().getId(),
                perfil.getUsuarioEmail().getCaracteres(),
                perfil.getUsername(),
                perfil.isEstado(),
                perfil.getCriacao(),
                perfil.getFoto(),
                planosTreinosJpa != null ? planosTreinosJpa : new ArrayList<>(),
                conquistasJpa != null ? conquistasJpa : new ArrayList<>(),
                metasJpa != null ? metasJpa : new ArrayList<>(),
                amigosJpa != null ? amigosJpa : new ArrayList<>()
        );
    }

    public PerfilJpa toEntity(Perfil perfil) {
        // Versão simplificada sem relacionamentos - para uso quando não precisamos carregar relacionamentos
        return toEntity(perfil, null, null, null, null);
    }

    public Perfil toDomain(PerfilJpa entity) {
        Perfil perfil = new Perfil(
                new PerfilId(entity.getId()),
                new Email(entity.getUsuarioEmail()),
                entity.getUsername()
        );
        
        perfil.setEstado(entity.getEstado());
        perfil.setFoto(entity.getFoto());
        
        // Converter relacionamentos se existirem
        if (entity.getConquistas() != null) {
            entity.getConquistas().forEach(conquistaJpa -> {
                perfil.adicionarConquista(ConquistaMapper.toDomain(conquistaJpa));
            });
        }
        
        if (entity.getMetas() != null && metaMapper != null) {
            entity.getMetas().forEach(metaJpa -> {
                perfil.adicionarMeta(metaMapper.toDomain(metaJpa));
            });
        }
        
        // PlanosTreinos e Amigos são mais complexos e podem ser carregados via queries específicas
        // quando necessário

        return perfil;
    }
}


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
        // Se o ID for null ou 0, trata como null para que o Hibernate gere um novo ID
        Integer id = null;
        if (perfil.getId() != null && perfil.getId().getId() > 0) {
            id = perfil.getId().getId();
        }
        
        PerfilJpa perfilJpa = new PerfilJpa(
                id,
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
        perfilJpa.setConquistasSelecionadas(perfil.getConquistasSelecionadas());
        return perfilJpa;
    }

    public PerfilJpa toEntity(Perfil perfil) {
        // Versão simplificada sem relacionamentos - para uso quando não precisamos carregar relacionamentos
        return toEntity(perfil, null, null, null, null);
    }

    public Perfil toDomain(PerfilJpa entity) {
        if (entity == null) {
            throw new IllegalArgumentException("PerfilJpa não pode ser null");
        }
        
        // Se o ID for null, usar 0 temporariamente (será atualizado após salvar)
        PerfilId perfilId = entity.getId() != null ? new PerfilId(entity.getId()) : new PerfilId(0);
        
        Perfil perfil = new Perfil(
                perfilId,
                new Email(entity.getUsuarioEmail()),
                entity.getUsername()
        );
        
        perfil.setEstado(entity.getEstado());
        perfil.setFoto(entity.getFoto());
        perfil.setConquistasSelecionadas(entity.getConquistasSelecionadas());
        
        // Converter relacionamentos se existirem
        if (entity.getConquistas() != null && !entity.getConquistas().isEmpty()) {
            entity.getConquistas().forEach(conquistaJpa -> {
                perfil.adicionarConquista(ConquistaMapper.toDomain(conquistaJpa));
            });
        }
        
        if (entity.getMetas() != null && !entity.getMetas().isEmpty() && metaMapper != null) {
            entity.getMetas().forEach(metaJpa -> {
                perfil.adicionarMeta(metaMapper.toDomain(metaJpa));
            });
        }
        
        // PlanosTreinos e Amigos são mais complexos e podem ser carregados via queries específicas
        // quando necessário

        return perfil;
    }
}


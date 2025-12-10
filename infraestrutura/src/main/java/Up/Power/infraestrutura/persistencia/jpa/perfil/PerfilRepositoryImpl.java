package Up.Power.infraestrutura.persistencia.jpa.perfil;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import Up.Power.Email;
import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.infraestrutura.persistencia.jpa.consquista.ConquistaJpa;
import Up.Power.infraestrutura.persistencia.jpa.consquista.JpaConquistaRepository;

@Repository
public class PerfilRepositoryImpl implements PerfilRepository {

    private final JpaPerfilRepository repo;
    private final PerfilMapper mapper;
    private final JpaConquistaRepository conquistaRepo;

    public PerfilRepositoryImpl(
            JpaPerfilRepository repo, 
            PerfilMapper mapper,
            JpaConquistaRepository conquistaRepo
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.conquistaRepo = conquistaRepo;
    }

    @Override
    public Optional<Perfil> findById(PerfilId id) {
        return repo.findById(id.getId())
                .map(mapper::toDomain);
    }

    @Override
    public Perfil save(Perfil perfil) {
        // Se o perfil já existe, carregar a entidade existente com relacionamentos
        if (perfil.getId() != null && perfil.getId().getId() > 0) {
            Optional<PerfilJpa> existingOpt = repo.findById(perfil.getId().getId());
            if (existingOpt.isPresent()) {
                PerfilJpa existing = existingOpt.get();
                
                // Atualizar campos simples
                existing.setUsername(perfil.getUsername());
                existing.setFoto(perfil.getFoto());
                existing.setEstado(perfil.isEstado());
                existing.setConquistasSelecionadas(perfil.getConquistasSelecionadas());
                
                // IMPORTANTE: Sincronizar conquistas do objeto de domínio com a entidade JPA
                // Limpar conquistas existentes
                existing.getConquistas().clear();
                
                // Adicionar as conquistas do objeto de domínio
                for (Up.Power.Conquista conquista : perfil.getConquistas()) {
                    // Buscar a ConquistaJpa do banco pelo ID
                    Optional<ConquistaJpa> conquistaJpaOpt = conquistaRepo.findById(conquista.getId().getId());
                    if (conquistaJpaOpt.isPresent()) {
                        ConquistaJpa conquistaJpa = conquistaJpaOpt.get();
                        // Adicionar se ainda não estiver na lista (evitar duplicatas)
                        if (!existing.getConquistas().contains(conquistaJpa)) {
                            existing.getConquistas().add(conquistaJpa);
                        }
                    }
                }
                
                PerfilJpa saved = repo.save(existing);
                return mapper.toDomain(saved);
            }
        }
        
        // Para novos perfis, precisamos mapear as conquistas também
        // Buscar as ConquistaJpa correspondentes
        List<ConquistaJpa> conquistasJpa = new ArrayList<>();
        for (Up.Power.Conquista conquista : perfil.getConquistas()) {
            Optional<ConquistaJpa> conquistaJpaOpt = conquistaRepo.findById(conquista.getId().getId());
            if (conquistaJpaOpt.isPresent()) {
                conquistasJpa.add(conquistaJpaOpt.get());
            }
        }
        
        // Usar o mapper com as conquistas JPA
        PerfilJpa entity = mapper.toEntity(
            perfil,
            new ArrayList<>(), // planosTreinos - pode ser vazio para novos perfis
            conquistasJpa,     // conquistas
            new ArrayList<>(), // metas
            new ArrayList<>()  // amigos
        );
        PerfilJpa saved = repo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Perfil> findByUsuarioEmail(String usuarioEmail) {
        return repo.findByUsuarioEmail(usuarioEmail)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        // Verifica existência de amizade diretamente na tabela de join para evitar carregar coleções
        if (perfilId1 == null || perfilId2 == null) {
            return false;
        }

        Integer id1 = perfilId1.getId();
        Integer id2 = perfilId2.getId();

        if (id1 == null || id2 == null) {
            return false;
        }

        // Checa em ambas as direções (perfil -> amigo) para garantir compatibilidade
        boolean direct = repo.existsFriendship(id1, id2);
        boolean reverse = repo.existsFriendship(id2, id1);
        return direct || reverse;
    }

    @Override
    public List<Perfil> listarTodos() {
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deletarPorId(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public boolean existePorEmail(Email usuarioEmail) {
        return repo.existsByUsuarioEmail(usuarioEmail.getCaracteres());
    }

    @Override
    public void atualizar(Perfil perfil) {
        // O método save já faz update se o ID existir
        save(perfil);
    }

    @Override
    public void adicionarAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        if (perfilId1 == null || perfilId2 == null) {
            throw new IllegalArgumentException("PerfilIds não podem ser null");
        }
        
        if (perfilId1.equals(perfilId2)) {
            throw new IllegalArgumentException("Um perfil não pode ser amigo de si mesmo");
        }
        
        // Verificar se já são amigos
        if (existsAmizade(perfilId1, perfilId2)) {
            return; // Já são amigos, não precisa fazer nada
        }
        
        // Buscar ambos os perfis
        Optional<PerfilJpa> perfil1Opt = repo.findById(perfilId1.getId());
        Optional<PerfilJpa> perfil2Opt = repo.findById(perfilId2.getId());
        
        if (perfil1Opt.isEmpty() || perfil2Opt.isEmpty()) {
            throw new IllegalArgumentException("Um ou ambos os perfis não foram encontrados");
        }
        
        PerfilJpa perfil1 = perfil1Opt.get();
        PerfilJpa perfil2 = perfil2Opt.get();
        
        // Adicionar amizade bidirecional
        if (!perfil1.getAmigos().contains(perfil2)) {
            perfil1.getAmigos().add(perfil2);
        }
        if (!perfil2.getAmigos().contains(perfil1)) {
            perfil2.getAmigos().add(perfil1);
        }
        
        // Salvar ambos
        repo.save(perfil1);
        repo.save(perfil2);
    }

    @Override
    public void removerAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        if (perfilId1 == null || perfilId2 == null) {
            throw new IllegalArgumentException("PerfilIds não podem ser null");
        }
        
        // Buscar ambos os perfis
        Optional<PerfilJpa> perfil1Opt = repo.findById(perfilId1.getId());
        Optional<PerfilJpa> perfil2Opt = repo.findById(perfilId2.getId());
        
        if (perfil1Opt.isEmpty() || perfil2Opt.isEmpty()) {
            return; // Se não existem, não há nada para remover
        }
        
        PerfilJpa perfil1 = perfil1Opt.get();
        PerfilJpa perfil2 = perfil2Opt.get();
        
        // Remover amizade bidirecional
        perfil1.getAmigos().remove(perfil2);
        perfil2.getAmigos().remove(perfil1);
        
        // Salvar ambos
        repo.save(perfil1);
        repo.save(perfil2);
    }

    @Override
    public List<Perfil> listarAmigos(PerfilId perfilId) {
        if (perfilId == null) {
            throw new IllegalArgumentException("PerfilId não pode ser null");
        }
        
        // Buscar amigos diretos (perfil -> amigos)
        List<PerfilJpa> amigosJpa = repo.findAmigosDiretosByPerfilId(perfilId.getId());
        
        return amigosJpa.stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}


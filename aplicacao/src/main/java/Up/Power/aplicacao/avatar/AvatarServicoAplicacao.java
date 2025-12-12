package Up.Power.aplicacao.avatar;

import Up.Power.Avatar;
import Up.Power.aplicacao.conquista.ConquistaAvaliadorAplicacao;
import Up.Power.acessorio.AcessorioId;
import Up.Power.acessorio.AcessorioRepository;
import Up.Power.avatar.AvatarService;
import Up.Power.avatar.ExperienceService;
import Up.Power.avatar.AvatarId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AvatarServicoAplicacao {

    private final AvatarRepositorioAplicacao avatarRepositorio;
    private final AvatarService avatarDominioService;
    private final ExperienceService experienceService;
    private final ConquistaAvaliadorAplicacao conquistaAvaliador;
    private final AcessorioRepository acessorioRepository;

    public AvatarServicoAplicacao(
            AvatarRepositorioAplicacao avatarRepositorio,
            AvatarService avatarDominioService,
            ExperienceService experienceService,
            @Lazy ConquistaAvaliadorAplicacao conquistaAvaliador,
            AcessorioRepository acessorioRepository
    ) {
        this.avatarRepositorio = avatarRepositorio;
        this.avatarDominioService = avatarDominioService;
        this.experienceService = experienceService;
        this.conquistaAvaliador = conquistaAvaliador;
        this.acessorioRepository = acessorioRepository;
    }

    public AvatarResumo obterPorPerfilId(Integer perfilId) {
        return avatarRepositorio.obterPorPerfilId(perfilId)
                .map(avatar -> {
                    System.out.println("Avatar carregado - ID: " + avatar.getId().getId() + 
                                     ", Dinheiro: " + avatar.getDinheiro() + 
                                     ", PerfilId: " + avatar.getPerfil().getId());
                    return AvatarResumoAssembler.toResumo(avatar);
                })
                .orElse(null);
    }

    public void adicionarXp(AdicionarXpCommand command) {
        Optional<Avatar> avatarOpt = avatarRepositorio.obterPorPerfilId(command.perfilId());

        if (avatarOpt.isEmpty()) {
            throw new NoSuchElementException("Avatar para o Perfil " + command.perfilId() + " não encontrado.");
        }

        Avatar avatar = avatarOpt.get();
        
        // 1. Chama o serviço de domínio para aplicar a lógica de XP/Nível
        experienceService.adicionarXp(avatar, command.xpGanha());
        
        // 2. Persiste a entidade de domínio atualizada (salva novo Nível/XP)
        avatarRepositorio.salvar(avatar);
        
        // 3. Avaliar conquistas baseadas nos atributos atualizados do avatar
        try {
            conquistaAvaliador.avaliarEAdicionarConquistasPorAtributos(
                    command.perfilId(),
                    avatar
            );
        } catch (Exception e) {
            // Log do erro mas não interrompe a adição de XP
            System.err.println("Erro ao avaliar conquistas por atributos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // DTO para retornar atributos calculados na camada de aplicação
    public record AtributosCalculadosResumo(int forca, int resistencia, int agilidade) {}

    // Método para expor atributos calculados usando o AvatarService de domínio
    public AtributosCalculadosResumo obterAtributosCalculados(Integer avatarId) {
        AvatarId id = new AvatarId(avatarId); 
        

        int forca = avatarDominioService.getForca(id);
        int resistencia = avatarDominioService.getResistencia(id);
        int agilidade = avatarDominioService.getAgilidade(id);
        
        return new AtributosCalculadosResumo(forca, resistencia, agilidade);
    }

    public AvatarResumo equiparAcessorios(Integer avatarId, java.util.List<Integer> acessorioIds) {
        var avatarOpt = avatarRepositorio.obterPorId(new AvatarId(avatarId));
        if (avatarOpt.isEmpty()) {
            throw new NoSuchElementException("Avatar não encontrado: " + avatarId);
        }
        var avatar = avatarOpt.get();

        if (acessorioIds == null) {
            acessorioIds = java.util.Collections.emptyList();
        }

        // Garante que todo item do inventário tenha entrada na flag (default false)
        avatar.getAcessorios().forEach(acc -> {
            if (acc.getId() != null) {
                avatar.getAcessoriosEquipados().putIfAbsent(acc.getId().getId(), false);
            }
        });

        // Atualiza flags de equipado preservando inventário existente
        var idsUnicos = acessorioIds.stream().distinct().toList();

        // Regra: apenas 1 item equipado por subcategoria
        java.util.Map<String, Integer> escolhidosPorSubcat = new java.util.HashMap<>();

        for (Integer id : idsUnicos) {
            acessorioRepository.findById(new AcessorioId(id)).ifPresent(acc -> {
                String key = normalizarSubcat(acc.getSubcategoria());
                // guarda o primeiro de cada subcategoria
                escolhidosPorSubcat.putIfAbsent(key, id);
                // garante presença no inventário
                avatar.adicionarAcessorio(acc, false);
            });
        }

        // Zera flags e aplica apenas os escolhidos
        avatar.getAcessoriosEquipados().replaceAll((k, v) -> false);
        escolhidosPorSubcat.values().forEach(id -> avatar.getAcessoriosEquipados().put(id, true));

        avatarRepositorio.salvar(avatar);
        return AvatarResumoAssembler.toResumo(avatar);
    }

    public AvatarResumo equiparAcessoriosPorPerfil(Integer perfilId, java.util.List<Integer> acessorioIds) {
        var avatarOpt = avatarRepositorio.obterPorPerfilId(perfilId);
        if (avatarOpt.isEmpty()) {
            throw new NoSuchElementException("Avatar não encontrado para o perfil: " + perfilId);
        }
        return equiparAcessorios(avatarOpt.get().getId().getId(), acessorioIds);
    }

    public AvatarResumo obterPorId(Integer avatarId) {
        var avatarOpt = avatarRepositorio.obterPorId(new AvatarId(avatarId));
        if (avatarOpt.isEmpty()) {
            throw new NoSuchElementException("Avatar não encontrado: " + avatarId);
        }
        return AvatarResumoAssembler.toResumo(avatarOpt.get());
    }

    public void adicionarForca(Integer perfilId, int bonusDeForca) {
        Optional<Avatar> avatarOpt = avatarRepositorio.obterPorPerfilId(perfilId);

        if (avatarOpt.isEmpty()) {
            throw new NoSuchElementException("Avatar para o Perfil " + perfilId + " não encontrado.");
        }

        Avatar avatar = avatarOpt.get();
        
        // Chama o serviço de domínio para adicionar força
        experienceService.adicionarForca(avatar, bonusDeForca);
        
        // Persiste a entidade de domínio atualizada
        avatarRepositorio.salvar(avatar);
        
        // Avaliar conquistas baseadas nos atributos atualizados do avatar
        try {
            conquistaAvaliador.avaliarEAdicionarConquistasPorAtributos(perfilId, avatar);
        } catch (Exception e) {
            // Log do erro mas não interrompe a adição de força
            System.err.println("Erro ao avaliar conquistas por atributos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String normalizarSubcat(String subcat) {
        if (subcat == null || subcat.isBlank()) return "__sem_subcat__";
        return subcat.trim().toLowerCase();
    }
}
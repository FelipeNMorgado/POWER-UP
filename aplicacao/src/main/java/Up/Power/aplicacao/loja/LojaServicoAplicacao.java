package Up.Power.aplicacao.loja;

import Up.Power.Acessorio;
import Up.Power.acessorio.AcessorioId;
import Up.Power.acessorio.AcessorioRepository;
import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LojaServicoAplicacao {

    private final AcessorioRepository acessorioRepository;
    private final AvatarRepository avatarRepository;

    public LojaServicoAplicacao(
            AcessorioRepository acessorioRepository,
            AvatarRepository avatarRepository) {
        this.acessorioRepository = acessorioRepository;
        this.avatarRepository = avatarRepository;
    }

    public List<ItemLojaResumo> listarItens() {
        List<Acessorio> acessorios = acessorioRepository.findAll();
        System.out.println("Total de acessórios encontrados: " + acessorios.size());
        return acessorios.stream()
                .map(this::toItemLojaResumo)
                .collect(Collectors.toList());
    }

    public Optional<ItemLojaResumo> obterPorId(Integer id) {
        return acessorioRepository.findById(new AcessorioId(id))
                .map(this::toItemLojaResumo);
    }

    public void comprarItem(Integer avatarId, Integer acessorioId) {
        var avatarOpt = avatarRepository.findById(new AvatarId(avatarId));
        if (avatarOpt.isEmpty()) {
            throw new IllegalArgumentException("Avatar não encontrado");
        }

        var acessorioOpt = acessorioRepository.findById(new AcessorioId(acessorioId));
        if (acessorioOpt.isEmpty()) {
            throw new IllegalArgumentException("Acessório não encontrado");
        }

        var avatar = avatarOpt.get();
        var acessorio = acessorioOpt.get();

        // Verificar se o avatar tem dinheiro suficiente
        if (avatar.getDinheiro() < acessorio.getPreco()) {
            throw new IllegalStateException("Dinheiro insuficiente para comprar este item");
        }

        // Verificar se o avatar já possui este acessório
        boolean jaPossui = avatar.getAcessorios().stream()
                .anyMatch(a -> a.getId().equals(acessorio.getId()));

        if (jaPossui) {
            throw new IllegalStateException("Você já possui este acessório");
        }

        // Deduzir o dinheiro
        avatar.setDinheiro(avatar.getDinheiro() - acessorio.getPreco());

        // Adicionar o acessório ao inventário do avatar, não equipado
        avatar.adicionarAcessorio(acessorio, false);

        // Salvar o avatar atualizado
        avatarRepository.save(avatar);
    }

    public List<ItemLojaResumo> listarItensPorCategoria(String categoria) {
        // Por enquanto, retorna todos os itens
        // Pode ser implementado filtro por categoria no futuro
        return listarItens();
    }

    private ItemLojaResumo toItemLojaResumo(Acessorio acessorio) {
        return new ItemLojaResumo(
                acessorio.getId().getId(),
                acessorio.getIcone() != null ? acessorio.getIcone() : "",
                acessorio.getImagem() != null ? acessorio.getImagem() : "",
                acessorio.getNome() != null ? acessorio.getNome() : "",
                acessorio.getPreco(),
                acessorio.getQualidade(),
                acessorio.getCategoria(),
                acessorio.getSubcategoria(),
                List.of() // Lista vazia, pois não há mais acessórios aninhados
        );
    }
}


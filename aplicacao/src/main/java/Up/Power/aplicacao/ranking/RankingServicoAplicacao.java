package Up.Power.aplicacao.ranking;

import Up.Power.Avatar;
import Up.Power.Email;
import Up.Power.Perfil;
import Up.Power.aplicacao.equipe.EquipeRepositorioAplicacao;
import Up.Power.avatar.AvatarRepository;
import Up.Power.equipe.EquipeId;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RankingServicoAplicacao {

    private final PerfilRepository perfilRepository;
    private final AvatarRepository avatarRepository;
    private final PerfilService perfilService;
    private final EquipeRepositorioAplicacao equipeRepositorioAplicacao;

    public RankingServicoAplicacao(
            PerfilRepository perfilRepository,
            AvatarRepository avatarRepository,
            PerfilService perfilService,
            EquipeRepositorioAplicacao equipeRepositorioAplicacao
    ) {
        this.perfilRepository = perfilRepository;
        this.avatarRepository = avatarRepository;
        this.perfilService = perfilService;
        this.equipeRepositorioAplicacao = equipeRepositorioAplicacao;
    }

    public List<RankingEntrada> rankingGlobal() {
        List<Perfil> perfis = perfilRepository.listarTodos();
        List<RankingEntrada> entradas = perfis.stream()
                .map(this::toEntradaComAvatar)
                .collect(Collectors.toCollection(ArrayList::new));
        ordenarEAtribuirPosicao(entradas);
        return entradas;
    }

    public List<RankingEntrada> rankingAmigos(String emailUsuario) {
        if (emailUsuario == null || emailUsuario.isBlank()) {
            return List.of();
        }

        Optional<Perfil> perfilOpt = perfilRepository.findByUsuarioEmail(emailUsuario);
        if (perfilOpt.isEmpty()) {
            return List.of();
        }

        Perfil perfilUsuario = perfilOpt.get();
        PerfilId perfilId = perfilUsuario.getId();
        if (perfilId == null) {
            return List.of();
        }

        // Lista de amigos + o próprio usuário
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfilUsuario);
        perfis.addAll(perfilService.listarAmigos(perfilId));

        // Remover duplicatas por id
        perfis = perfis.stream()
                .filter(p -> p.getId() != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(p -> p.getId().getId(), p -> p, (p1, p2) -> p1),
                        m -> new ArrayList<>(m.values())
                ));

        List<RankingEntrada> entradas = perfis.stream()
                .map(this::toEntradaComAvatar)
                .collect(Collectors.toCollection(ArrayList::new));
        ordenarEAtribuirPosicao(entradas);
        return entradas;
    }

    public List<RankingEntrada> rankingEquipe(Integer equipeId) {
        if (equipeId == null) {
            return List.of();
        }
        var equipeOpt = equipeRepositorioAplicacao.obterPorId(new EquipeId(equipeId));
        if (equipeOpt.isEmpty()) {
            return List.of();
        }

        var equipe = equipeOpt.get();
        List<RankingEntrada> entradas = new ArrayList<>();

        for (Email emailMembro : equipe.getUsuariosEmails()) {
            Optional<Perfil> perfilOpt = perfilRepository.findByUsuarioEmail(emailMembro.getCaracteres());
            perfilOpt.ifPresent(perfil -> entradas.add(toEntradaComAvatar(perfil, equipeId)));
        }

        ordenarEAtribuirPosicao(entradas);
        return entradas;
    }

    private RankingEntrada toEntradaComAvatar(Perfil perfil) {
        return toEntradaComAvatar(perfil, null);
    }

    private RankingEntrada toEntradaComAvatar(Perfil perfil, Integer equipeId) {
        if (perfil == null || perfil.getId() == null) {
            return new RankingEntrada(null, null, null, null, 1, 0, 0, equipeId);
        }

        Optional<Avatar> avatarOpt = avatarRepository.findByPerfilId(perfil.getId());

        int nivel = avatarOpt.map(Avatar::getNivel).orElse(1);
        int experiencia = avatarOpt.map(Avatar::getExperiencia).orElse(0);
        int xpTotal = calcularXpTotal(nivel, experiencia);

        return new RankingEntrada(
                perfil.getId().getId(),
                perfil.getUsuarioEmail() != null ? perfil.getUsuarioEmail().getCaracteres() : null,
                perfil.getUsername(),
                perfil.getFoto(),
                nivel,
                xpTotal,
                0, // será preenchido após ordenar
                equipeId
        );
    }

    private int calcularXpTotal(int nivel, int experiencia) {
        int nivelAjustado = Math.max(nivel, 1);
        int xpNivel = Math.max(nivelAjustado - 1, 0) * 100;
        return xpNivel + Math.max(experiencia, 0);
    }

    private void ordenarEAtribuirPosicao(List<RankingEntrada> entradas) {
        entradas.sort(Comparator
                .comparingInt(RankingEntrada::xpTotal).reversed()
                .thenComparing(e -> Optional.ofNullable(e.username()).orElse(""))
                .thenComparing(e -> Optional.ofNullable(e.email()).orElse(""))
        );

        for (int i = 0; i < entradas.size(); i++) {
            RankingEntrada atual = entradas.get(i);
            entradas.set(i, new RankingEntrada(
                    atual.perfilId(),
                    atual.email(),
                    atual.username(),
                    atual.foto(),
                    atual.nivel(),
                    atual.xpTotal(),
                    i + 1,
                    atual.equipeId()
            ));
        }
    }
}


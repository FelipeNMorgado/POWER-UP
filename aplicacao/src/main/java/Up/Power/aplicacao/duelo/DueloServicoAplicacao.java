package Up.Power.aplicacao.duelo;

import Up.Power.Duelo;
import Up.Power.duelo.DueloId;
import Up.Power.duelo.DueloRepository;
import Up.Power.duelo.DueloService;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.avatar.AvatarService;
import org.springframework.stereotype.Service;

@Service
public class DueloServicoAplicacao {

    private final DueloRepositorioAplicacao dueloRepositorioAplicacao;
    private final DueloService dominioService;

    public DueloServicoAplicacao(
            DueloRepositorioAplicacao dueloRepositorioAplicacao,
            DueloRepository dueloRepository,
            PerfilRepository perfilRepository,
            Up.Power.avatar.AvatarRepository avatarRepository
    ) {
        this.dueloRepositorioAplicacao = dueloRepositorioAplicacao;
        AvatarService avatarService = new AvatarService(avatarRepository);
        this.dominioService = new DueloService(dueloRepository, avatarService, perfilRepository, avatarRepository);
    }

    public DueloResumo realizarDuelo(RealizarDueloCommand command) {
        Duelo duelo = dominioService.realizarDuelo(
                new PerfilId(command.desafiantePerfilId()),
                new PerfilId(command.desafiadoPerfilId())
        );
        return DueloResumoAssembler.toResumo(duelo);
    }

    public DueloResumo obterPorId(Integer id) {
        return dueloRepositorioAplicacao.obterPorId(new DueloId(id))
                .map(DueloResumoAssembler::toResumo)
                .orElse(null);
    }

    public DueloResumo ultimoEntrePerfis(Integer perfilId1, Integer perfilId2) {
        return dueloRepositorioAplicacao.ultimoEntrePerfis(perfilId1, perfilId2)
                .map(DueloResumoAssembler::toResumo)
                .orElse(null);
    }
}

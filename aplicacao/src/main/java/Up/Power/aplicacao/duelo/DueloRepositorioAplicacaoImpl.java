package Up.Power.aplicacao.duelo;

import Up.Power.avatar.AvatarRepository;
import Up.Power.duelo.Duelo;
import Up.Power.duelo.DueloId;
import Up.Power.duelo.DueloRepository;
import Up.Power.perfil.PerfilId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DueloRepositorioAplicacaoImpl implements DueloRepositorioAplicacao {

    private final DueloRepository dueloRepository;
    private final AvatarRepository avatarRepository;

    public DueloRepositorioAplicacaoImpl(DueloRepository dueloRepository, AvatarRepository avatarRepository) {
        this.dueloRepository = dueloRepository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public Optional<Duelo> obterPorId(DueloId id) {
        return dueloRepository.findById(id);
    }

    @Override
    public Optional<Duelo> ultimoEntrePerfis(Integer perfilId1, Integer perfilId2) {
        var avatar1 = avatarRepository.findByPerfilId(new PerfilId(perfilId1));
        var avatar2 = avatarRepository.findByPerfilId(new PerfilId(perfilId2));
        if (avatar1.isEmpty() || avatar2.isEmpty()) return Optional.empty();
        return dueloRepository.findLastDuelBetween(avatar1.get().getId(), avatar2.get().getId());
    }

    @Override
    public Duelo salvar(Duelo duelo) {
        return dueloRepository.save(duelo);
    }
}
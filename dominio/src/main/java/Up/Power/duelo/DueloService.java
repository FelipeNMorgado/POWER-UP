package Up.Power.duelo;

import Up.Power.*;
import Up.Power.avatar.*;
import Up.Power.perfil.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class DueloService {
    private final DueloRepository dueloRepository;
    private final AvatarService avatarService;
    private final PerfilRepository perfilRepository;
    private final AvatarRepository avatarRepository;

    public DueloService(DueloRepository dueloRepository, AvatarService avatarService, PerfilRepository perfilRepository, AvatarRepository avatarRepository) {
        this.dueloRepository = dueloRepository;
        this.avatarService = avatarService;
        this.perfilRepository = perfilRepository;
        this.avatarRepository = avatarRepository;
    }

    public Duelo realizarDuelo(PerfilId desafianteId, PerfilId desafiadoId) {

        validarAmizade(desafianteId, desafiadoId);

        Avatar avatar1 = avatarRepository.findByPerfilId(desafianteId).orElseThrow();
        Avatar avatar2 = avatarRepository.findByPerfilId(desafiadoId).orElseThrow();

        validarCooldownDuelo(avatar1.getId(), avatar2.getId());

        int vitoriasAvatar1 = 0;
        if (avatarService.getForca(avatar1.getId()) > avatarService.getForca(avatar2.getId())) vitoriasAvatar1++;
        if (avatarService.getResistencia(avatar1.getId()) > avatarService.getResistencia(avatar2.getId())) vitoriasAvatar1++;
        if (avatarService.getAgilidade(avatar1.getId()) > avatarService.getAgilidade(avatar2.getId())) vitoriasAvatar1++;

        String resultado;
        if (vitoriasAvatar1 >= 2) {
            resultado = "VITORIA_DESAFIANTE";
        } else {
            resultado = "VITORIA_DESAFIADO";
        }

        Duelo novoDuelo = new Duelo(avatar1.getId(), avatar2.getId());
        novoDuelo.setResultado(resultado);
        return dueloRepository.save(novoDuelo);
    }

    private void validarCooldownDuelo(AvatarId avatarId1, AvatarId avatarId2) {
        Optional<Duelo> ultimoDuelo = dueloRepository.findLastDuelBetween(avatarId1, avatarId2);

        if (ultimoDuelo.isPresent()) {
            long diasDesdeUltimoDuelo = ChronoUnit.DAYS.between(ultimoDuelo.get().getDataDuelo(), LocalDateTime.now());
            if (diasDesdeUltimoDuelo < 7) {
                throw new IllegalStateException("Você deve esperar " + (7 - diasDesdeUltimoDuelo) + " dias para desafiar este amigo novamente.");
            }
        }
    }

    private void validarAmizade(PerfilId desafianteId, PerfilId desafiadoId) {
        if (!perfilRepository.existsAmizade(desafianteId, desafiadoId)) {
            throw new IllegalStateException("Só é possível duelar com amigos.");
        }
    }


}
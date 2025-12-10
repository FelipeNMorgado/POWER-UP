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

        Avatar avatar1 = avatarRepository.findByPerfilId(desafianteId)
                .orElseThrow(() -> new IllegalStateException("Avatar do desafiante não encontrado. O perfil precisa ter um avatar cadastrado."));
        Avatar avatar2 = avatarRepository.findByPerfilId(desafiadoId)
                .orElseThrow(() -> new IllegalStateException("Avatar do desafiado não encontrado. O perfil precisa ter um avatar cadastrado."));

        validarCooldownDuelo(avatar1.getId(), avatar2.getId());

        String resultado = calcularResultadoDuelo(avatar1, avatar2);

        Duelo novoDuelo = new Duelo(avatar1.getId(), avatar2.getId());
        novoDuelo.setResultado(resultado);
        novoDuelo.setDataDuelo(LocalDateTime.now());
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

    String calcularResultadoDuelo(Avatar avatar1, Avatar avatar2){
        int forca1 = avatarService.getForca(avatar1.getId());
        int res1 = avatarService.getResistencia(avatar1.getId());
        int agi1 = avatarService.getAgilidade(avatar1.getId());
        int forca2 = avatarService.getForca(avatar2.getId());
        int res2 = avatarService.getResistencia(avatar2.getId());
        int agi2 = avatarService.getAgilidade(avatar2.getId());

        int vitoriasAvatar1 = 0;
        int vitoriasAvatar2 = 0;

        if (forca1 > forca2) {
            vitoriasAvatar1++;
        } else if (forca2 > forca1) {
            vitoriasAvatar2++;
        }

        if (res1 > res2) {
            vitoriasAvatar1++;
        } else if (res2 > res1) {
            vitoriasAvatar2++;
        }

        if (agi1 > agi2) {
            vitoriasAvatar1++;
        } else if (agi2 > agi1) {
            vitoriasAvatar2++;
        }

        String resultado;
        if (vitoriasAvatar1 > vitoriasAvatar2) {
            resultado = "VITORIA_DESAFIANTE(A1)";
        } else if (vitoriasAvatar2 > vitoriasAvatar1) {
            resultado = "VITORIA_DESAFIADO(A2)";
        } else {
            resultado = "EMPATE";
        }

        return resultado;
    }


}
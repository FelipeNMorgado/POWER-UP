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

    public Duelo iniciarDuelo(PerfilId desafianteId, PerfilId desafiadoId) {
        // 1. Validação de Amizade
        Perfil desafiante = perfilRepository.findById(desafianteId).orElseThrow();
        boolean saoAmigos = desafiante.getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfilRepository.findById(desafiadoId).get().getUsuarioEmail()));
        if (!saoAmigos) throw new IllegalStateException("Só é possível duelar com amigos.");

        // 2. Encontrar os Avatares
        Avatar avatar1 = avatarRepository.findByPerfilId(desafianteId).orElseThrow();
        Avatar avatar2 = avatarRepository.findByPerfilId(desafiadoId).orElseThrow();

        // 3. Validação do Cooldown de 1 Semana
        Optional<Duelo> ultimoDuelo = dueloRepository.findLastDuelBetween(avatar1.getId(), avatar2.getId());
        if (ultimoDuelo.isPresent()) {
            long diasDesdeUltimoDuelo = ChronoUnit.DAYS.between(ultimoDuelo.get().getDataDuelo(), LocalDateTime.now());
            if (diasDesdeUltimoDuelo < 7) {
                throw new IllegalStateException("Você deve esperar " + (7 - diasDesdeUltimoDuelo) + " dias para desafiar este amigo novamente.");
            }
        }

        // 4. Lógica do Combate (Melhor de 3)
        int vitoriasAvatar1 = 0;
        if (avatarService.getForca(avatar1.getId()) > avatarService.getForca(avatar2.getId())) vitoriasAvatar1++;
        if (avatarService.getResistencia(avatar1.getId()) > avatarService.getResistencia(avatar2.getId())) vitoriasAvatar1++;
        if (avatarService.getAgilidade(avatar1.getId()) > avatarService.getAgilidade(avatar2.getId())) vitoriasAvatar1++;

        // 5. Determinar Resultado
        String resultado;
        if (vitoriasAvatar1 >= 2) {
            resultado = "VITORIA_DESAFIANTE";
        } else {
            resultado = "VITORIA_DESAFIADO";
        }

        // 6. Criar e Salvar o Duelo
        Duelo novoDuelo = new Duelo(avatar1.getId(), avatar2.getId());
        novoDuelo.setResultado(resultado);
        return dueloRepository.save(novoDuelo);
    }
}
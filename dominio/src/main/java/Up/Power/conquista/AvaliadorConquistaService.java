package Up.Power.conquista;

import Up.Power.Avatar;
import Up.Power.Conquista;
import Up.Power.Perfil;
import Up.Power.Treino;
import Up.Power.avatar.AvatarService;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;

import java.util.List;

/**
 * Serviço responsável por avaliar automaticamente se um perfil conquistou uma conquista
 * baseado em critérios de treino e atributos do avatar.
 */
public class AvaliadorConquistaService {

    private final ConquistaRepository conquistaRepository;
    private final AvatarService avatarService;

    public AvaliadorConquistaService(ConquistaRepository conquistaRepository, AvatarService avatarService) {
        this.conquistaRepository = conquistaRepository;
        this.avatarService = avatarService;
    }

    /**
     * Avalia se um treino recém-registrado desbloqueia alguma conquista para o perfil.
     * @param perfil Perfil que realizou o treino
     * @param treino Treino realizado
     * @return Lista de conquistas conquistadas (pode estar vazia)
     */
    public List<Conquista> avaliarConquistasPorTreino(Perfil perfil, Treino treino) {
        List<Conquista> conquistasDesbloqueadas = new java.util.ArrayList<>();
        
        // Buscar todas as conquistas ativas
        List<Conquista> todasConquistas = conquistaRepository.listarAtivas();
        
        for (Conquista conquista : todasConquistas) {
            // Verificar se o perfil já tem essa conquista (comparando por ID)
            boolean jaTemConquista = perfil.getConquistas().stream()
                    .anyMatch(c -> c.getId().equals(conquista.getId()));
            if (jaTemConquista) {
                continue;
            }
            
            // Verificar se a conquista está relacionada ao exercício do treino
            if (conquista.getExercicio() != null && 
                treino.getExercicio() != null &&
                conquista.getExercicio().equals(treino.getExercicio())) {
                
                // Verificar critérios baseados em treino
                if (atendeCriteriosTreino(conquista, treino)) {
                    conquistasDesbloqueadas.add(conquista);
                }
            }
        }
        
        return conquistasDesbloqueadas;
    }

    /**
     * Avalia se os atributos do avatar desbloqueiam alguma conquista para o perfil.
     * @param perfil Perfil a ser avaliado
     * @param avatar Avatar com atributos atualizados
     * @return Lista de conquistas conquistadas (pode estar vazia)
     */
    public List<Conquista> avaliarConquistasPorAtributos(Perfil perfil, Avatar avatar) {
        List<Conquista> conquistasDesbloqueadas = new java.util.ArrayList<>();
        
        // Buscar todas as conquistas ativas
        List<Conquista> todasConquistas = conquistaRepository.listarAtivas();
        
        for (Conquista conquista : todasConquistas) {
            // Verificar se o perfil já tem essa conquista (comparando por ID)
            boolean jaTemConquista = perfil.getConquistas().stream()
                    .anyMatch(c -> c.getId().equals(conquista.getId()));
            if (jaTemConquista) {
                continue;
            }
            
            // Verificar critérios baseados em atributos
            if (atendeCriteriosAtributos(conquista, avatar)) {
                conquistasDesbloqueadas.add(conquista);
            }
        }
        
        return conquistasDesbloqueadas;
    }

    /**
     * Verifica se um treino atende aos critérios de uma conquista.
     */
    private boolean atendeCriteriosTreino(Conquista conquista, Treino treino) {
        // Verificar peso mínimo
        if (conquista.getPesoMinimo() != null) {
            if (treino.getPeso() < conquista.getPesoMinimo()) {
                return false;
            }
        }
        
        // Verificar repetições mínimas
        if (conquista.getRepeticoesMinimas() != null) {
            if (treino.getRepeticoes() < conquista.getRepeticoesMinimas()) {
                return false;
            }
        }
        
        // Verificar séries mínimas
        if (conquista.getSeriesMinimas() != null) {
            if (treino.getSeries() < conquista.getSeriesMinimas()) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Verifica se os atributos do avatar atendem aos critérios de uma conquista.
     */
    private boolean atendeCriteriosAtributos(Conquista conquista, Avatar avatar) {
        // Se não há critério de atributo, não avalia
        if (conquista.getAtributoMinimo() == null || conquista.getTipoAtributo() == null) {
            return false;
        }
        
        int valorAtributo = 0;
        String tipoAtributo = conquista.getTipoAtributo().toLowerCase();
        
        switch (tipoAtributo) {
            case "forca":
                valorAtributo = avatar.getForca();
                break;
            case "resistencia":
                valorAtributo = avatarService.getResistencia(avatar.getId());
                break;
            case "agilidade":
                valorAtributo = avatarService.getAgilidade(avatar.getId());
                break;
            case "nivel":
                valorAtributo = avatar.getNivel();
                break;
            default:
                return false;
        }
        
        return valorAtributo >= conquista.getAtributoMinimo();
    }
}


package Up.Power.aplicacao.conquista;

import Up.Power.Avatar;
import Up.Power.Conquista;
import Up.Power.Perfil;
import Up.Power.Treino;
import Up.Power.avatar.AvatarService;
import Up.Power.conquista.AvaliadorConquistaService;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de aplicação que coordena a avaliação automática de conquistas
 * e adiciona as conquistas ao perfil quando os critérios são atendidos.
 */
@Service
public class ConquistaAvaliadorAplicacao {

    private final AvaliadorConquistaService avaliadorConquistaService;
    private final PerfilRepository perfilRepository;
    private final ConquistaRepository conquistaRepository;

    public ConquistaAvaliadorAplicacao(
            ConquistaRepository conquistaRepository,
            PerfilRepository perfilRepository,
            AvatarService avatarService
    ) {
        this.conquistaRepository = conquistaRepository;
        this.perfilRepository = perfilRepository;
        this.avaliadorConquistaService = new AvaliadorConquistaService(conquistaRepository, avatarService);
    }

    /**
     * Avalia e adiciona conquistas ao perfil baseado em um treino realizado.
     * @param perfilId ID do perfil
     * @param treino Treino realizado
     * @return Lista de conquistas conquistadas
     */
    public List<Conquista> avaliarEAdicionarConquistasPorTreino(Integer perfilId, Treino treino) {
        Perfil perfil = perfilRepository.findById(new PerfilId(perfilId))
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado: " + perfilId));
        
        List<Conquista> conquistasDesbloqueadas = avaliadorConquistaService.avaliarConquistasPorTreino(perfil, treino);
        
        // Adicionar conquistas ao perfil
        for (Conquista conquista : conquistasDesbloqueadas) {
            perfil.adicionarConquista(conquista);
        }
        
        // Salvar perfil atualizado
        if (!conquistasDesbloqueadas.isEmpty()) {
            perfilRepository.save(perfil);
        }
        
        return conquistasDesbloqueadas;
    }

    /**
     * Avalia e adiciona conquistas ao perfil baseado nos atributos do avatar.
     * @param perfilId ID do perfil
     * @param avatar Avatar com atributos atualizados
     * @return Lista de conquistas conquistadas
     */
    public List<Conquista> avaliarEAdicionarConquistasPorAtributos(Integer perfilId, Avatar avatar) {
        Perfil perfil = perfilRepository.findById(new PerfilId(perfilId))
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado: " + perfilId));
        
        List<Conquista> conquistasDesbloqueadas = avaliadorConquistaService.avaliarConquistasPorAtributos(perfil, avatar);
        
        // Adicionar conquistas ao perfil
        for (Conquista conquista : conquistasDesbloqueadas) {
            perfil.adicionarConquista(conquista);
        }
        
        // Salvar perfil atualizado
        if (!conquistasDesbloqueadas.isEmpty()) {
            perfilRepository.save(perfil);
        }
        
        return conquistasDesbloqueadas;
    }
}



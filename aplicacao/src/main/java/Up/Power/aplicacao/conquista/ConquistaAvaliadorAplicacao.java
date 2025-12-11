package Up.Power.aplicacao.conquista;

import Up.Power.Avatar;
import Up.Power.Conquista;
import Up.Power.Perfil;
import Up.Power.Treino;
import Up.Power.aplicacao.eventos.AtributosAtualizadosEvent;
import Up.Power.aplicacao.eventos.TreinoConcluidoEvent;
import Up.Power.avatar.AvatarService;
import Up.Power.conquista.AvaliadorConquistaService;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// ADIÇÃO 1: Implementar a interface ConquistaObserver
@Service
public class ConquistaAvaliadorAplicacao implements ConquistaObserver {

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

    // -------------------------------------------------------------------------
    // ADIÇÃO 2: Implementação do método UPDATE (O ponto de entrada do Observer)
    // -------------------------------------------------------------------------
    @Override
    public void update(String eventType, Object eventData) {
        // Assume que eventData é um Map ou um DTO que contém todos os dados.

        switch (eventType) {
            case "TREINO_CONCLUIDO":
                // É necessário converter o Object eventData para um formato conhecido (DTO).
                // Por exemplo: TreinoConcluidoEvent event = (TreinoConcluidoEvent) eventData;

                // Exemplo de cast (você deve definir a classe TreinoConcluidoEvent)
                if (eventData instanceof TreinoConcluidoEvent event) {
                    this.avaliarEAdicionarConquistasPorTreino(event.getPerfilId(), event.getTreino());
                }
                break;

            case "ATRIBUTOS_ATUALIZADOS":
                // Exemplo de cast
                if (eventData instanceof AtributosAtualizadosEvent event) {
                    this.avaliarEAdicionarConquistasPorAtributos(event.getPerfilId(), event.getAvatar());
                }
                break;

            default:
                // Ignorar outros eventos não relevantes para este Avaliador
                break;
        }
    }

    // -------------------------------------------------------------------------
    // MODIFICAÇÃO 3: Os métodos de avaliação são agora privados, pois são chamados
    // internamente pelo método 'update()'.
    // -------------------------------------------------------------------------

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
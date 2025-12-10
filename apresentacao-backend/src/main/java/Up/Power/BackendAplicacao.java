package Up.Power;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import Up.Power.avatar.AvatarRepository;
import Up.Power.avatar.AvatarService;
import Up.Power.avatar.ExperienceService;
import Up.Power.avatar.ExperienceServiceImpl;
import Up.Power.meta.RewardService;
import Up.Power.meta.RewardServiceImpl;
import Up.Power.perfil.PerfilRepository;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.planoTreino.PlanoTreinoService;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.conquista.ConquistaService;
import Up.Power.rivalidade.RivalidadeRepository;
import Up.Power.rivalidade.RivalidadeService;
import Up.Power.usuario.UsuarioRepository;
import Up.Power.usuario.UsuarioService;
import Up.Power.perfil.PerfilService;
import Up.Power.feedback.FeedbackService;
import Up.Power.feedback.FeedbackRepository;

@SpringBootApplication(scanBasePackages = {
    "Up.Power.aplicacao",
    "Up.Power.infraestrutura",
    "Up.Power.apresentacao"
})
@EntityScan(basePackages = "Up.Power.infraestrutura.persistencia.jpa")
@EnableJpaRepositories(basePackages = "Up.Power.infraestrutura.persistencia.jpa")
public class BackendAplicacao {
    
    @Bean
    public RivalidadeService rivalidadeService(
            RivalidadeRepository rivalidadeRepository,
            PerfilRepository perfilRepository) {
        return new RivalidadeService(rivalidadeRepository, perfilRepository);
    }
    
    @Bean
    public PlanoTreinoService planoTreinoService(PlanoTreinoRepository planoTreinoRepository) {
        return new PlanoTreinoService(planoTreinoRepository);
    }
    
    @Bean
    public AvatarService avatarService(AvatarRepository avatarRepository) {
        return new AvatarService(avatarRepository);
    }
    
    @Bean
    public ExperienceService experienceService() {
        return new ExperienceServiceImpl();
    }
    
    @Bean
    public RewardService rewardService() {
        return new RewardServiceImpl();
    }

    @Bean
    public PerfilService perfilService(PerfilRepository perfilRepository) {
        return new PerfilService(perfilRepository);
    }

    @Bean
    public UsuarioService usuarioService(UsuarioRepository usuarioRepository) {
        return new UsuarioService(usuarioRepository);
    }

    @Bean
    public ConquistaService conquistaService(ConquistaRepository conquistaRepository) {
        return new ConquistaService(conquistaRepository);
    }

    @Bean
    public FeedbackService feedbackService(FeedbackRepository feedbackRepository) {
        return new FeedbackService(feedbackRepository);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(BackendAplicacao.class, args);
    }
}

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
import Up.Power.perfil.PerfilRepository;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.planoTreino.PlanoTreinoService;
import Up.Power.meta.RewardService;
import Up.Power.meta.RewardServiceImpl;
import Up.Power.rivalidade.RivalidadeRepository;
import Up.Power.rivalidade.RivalidadeService;

/**
 * Classe principal da aplicação Spring Boot.
 * Configura o escaneamento de componentes, entidades JPA e repositórios.
 */
@SpringBootApplication(scanBasePackages = {
    "Up.Power.aplicacao",
    "Up.Power.infraestrutura"
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
    
    public static void main(String[] args) {
        SpringApplication.run(BackendAplicacao.class, args);
    }
}

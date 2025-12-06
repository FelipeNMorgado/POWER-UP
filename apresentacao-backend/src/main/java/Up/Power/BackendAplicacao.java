package Up.Power;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import Up.Power.perfil.PerfilRepository;
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
    
    public static void main(String[] args) {
        SpringApplication.run(BackendAplicacao.class, args);
    }
}

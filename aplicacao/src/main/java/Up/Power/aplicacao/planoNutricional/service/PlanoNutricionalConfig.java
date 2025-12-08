package Up.Power.aplicacao.planoNutricional.service;

import Up.Power.aplicacao.planoNutricional.service.decorators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanoNutricionalConfig {

    @Bean
    public PlanoNutricionalApplicationService planoNutricionalService(
            RegistroInfoDecorator decoratorFinal
    ) {
        return decoratorFinal;
    }
}
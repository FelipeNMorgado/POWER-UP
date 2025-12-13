package Up.Power.aplicacao.planoNutricional.service;

import Up.Power.aplicacao.planoNutricional.service.decorators.*;
import Up.Power.refeicao.RefeicaoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PlanoNutricionalConfig {

    @Bean
    @Primary
    public PlanoNutricionalApplicationService planoNutricionalService(
            BasePlanoNutricionalService baseService,
            RefeicaoRepository refeicaoRepository
    ) {

        CalculoCaloriasDecorator calculoDecorator = new CalculoCaloriasDecorator(baseService, refeicaoRepository);
        CicloObjetivoDecorator cicloDecorator = new CicloObjetivoDecorator(calculoDecorator);
        RegistroInfoDecorator registroDecorator = new RegistroInfoDecorator(cicloDecorator);
        return registroDecorator;
    }
}
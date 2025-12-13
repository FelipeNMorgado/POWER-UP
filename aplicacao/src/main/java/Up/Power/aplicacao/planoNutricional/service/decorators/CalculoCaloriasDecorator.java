package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.PlanoNutricional;
import Up.Power.Refeicao;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.refeicao.RefeicaoRepository;

public class CalculoCaloriasDecorator extends PlanoNutricionalDecorator {

    private final RefeicaoRepository refeicaoRepository;

    public CalculoCaloriasDecorator(
            PlanoNutricionalApplicationService next,
            RefeicaoRepository refeicaoRepository
    ) {
        super(next);
        this.refeicaoRepository = refeicaoRepository;
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        System.out.println("[CALCULO_CALORIAS] Decorator: CalculoCalorias - iniciando");
        PlanoNutricional plano = next.criar(command);
        System.out.println("[CALCULO_CALORIAS] Plano recebido do próximo decorator. Aplicando cálculo de calorias...");
        aplicarCalculo(plano);
        System.out.println("[CALCULO_CALORIAS] Cálculo de calorias concluído");
        return plano;
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.modificar(command);
        aplicarCalculo(plano);
        return plano;
    }

    private void aplicarCalculo(PlanoNutricional plano) {
        System.out.println("[CALCULO_CALORIAS] Aplicando cálculo de calorias totais...");
        try {

            if (plano.getRefeicoes() == null || plano.getRefeicoes().isEmpty()) {
                System.out.println("[CALCULO_CALORIAS] Nenhuma refeição encontrada. Definindo calorias totais como 0");
                plano.definirCaloriasTotais(0);
                return;
            }
            
            System.out.println("[CALCULO_CALORIAS] Calculando calorias de " + plano.getRefeicoes().size() + " refeições...");
            int total = plano.getRefeicoes().stream()
                    .map(refeicaoId -> {
                        System.out.println("[CALCULO_CALORIAS] Buscando refeição ID: " + refeicaoId.getId());
                        return refeicaoRepository.obter(refeicaoId);
                    })
                    .filter(refeicao -> refeicao != null) // Filtrar refeições nulas
                    .mapToInt(refeicao -> {
                        int calorias = refeicao.getCaloriasTotais();
                        System.out.println("[CALCULO_CALORIAS] Refeição encontrada com " + calorias + " calorias");
                        return calorias;
                    })
                    .sum();

            System.out.println("[CALCULO_CALORIAS] Total de calorias calculado: " + total);
            plano.definirCaloriasTotais(total);
        } catch (Exception e) {

            System.err.println("[CALCULO_CALORIAS] ERRO ao calcular calorias:");
            System.err.println("[CALCULO_CALORIAS] Tipo: " + e.getClass().getName());
            System.err.println("[CALCULO_CALORIAS] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            e.printStackTrace();
            System.out.println("[CALCULO_CALORIAS] Definindo calorias totais como 0 devido ao erro");
            plano.definirCaloriasTotais(0);
        }
    }
}

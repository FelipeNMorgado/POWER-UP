package Up.Power.aplicacao.planoNutricional.service.decorators;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.planoNutricional.Objetivo;
import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;
// Removido @Component - será criado manualmente na configuração
public class CicloObjetivoDecorator extends PlanoNutricionalDecorator {

    public CicloObjetivoDecorator(PlanoNutricionalApplicationService next) {
        super(next);
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        System.out.println("[CICLO_OBJETIVO] Decorator: CicloObjetivo - iniciando");
        PlanoNutricional plano = next.criar(command);
        System.out.println("[CICLO_OBJETIVO] Plano recebido. Verificando se precisa ajustar meta...");
        System.out.println("[CICLO_OBJETIVO] caloriasObjetivo do command: " + command.caloriasObjetivo());
        // Só ajustar meta se o usuário não forneceu uma meta personalizada
        if (command.caloriasObjetivo() == null || command.caloriasObjetivo() <= 0) {
            System.out.println("[CICLO_OBJETIVO] Meta não fornecida pelo usuário. Ajustando meta baseada no objetivo...");
            ajustarMeta(plano);
        } else {
            System.out.println("[CICLO_OBJETIVO] Meta fornecida pelo usuário (" + command.caloriasObjetivo() + "). Não ajustando.");
        }
        System.out.println("[CICLO_OBJETIVO] Decorator concluído");
        return plano;
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = next.modificar(command);
        // Para modificar, sempre ajustar meta se não houver refeições
        ajustarMeta(plano);
        return plano;
    }

    private void ajustarMeta(PlanoNutricional plano) {
        System.out.println("[CICLO_OBJETIVO] Ajustando meta de calorias...");
        System.out.println("[CICLO_OBJETIVO] Meta atual: " + plano.getCaloriasObjetivo());
        // Se já tem meta definida (maior que zero), não recalcular
        // Isso preserva a meta definida pelo usuário
        if (plano.getCaloriasObjetivo() > 0) {
            System.out.println("[CICLO_OBJETIVO] Meta já definida. Não recalculando.");
            return;
        }

        int total = plano.getCaloriasTotais();
        System.out.println("[CICLO_OBJETIVO] Calorias totais: " + total);
        System.out.println("[CICLO_OBJETIVO] Objetivo: " + plano.getObjetivo());
        
        // Se não há calorias totais (sem refeições), usar valores padrão baseados no objetivo
        if (total == 0) {
            int meta = switch (plano.getObjetivo()) {
                case Cutting -> 2000; // Valor padrão para Cutting
                case Bulking -> 2500; // Valor padrão para Bulking
            };
            System.out.println("[CICLO_OBJETIVO] Usando valor padrão: " + meta);
            plano.definirCaloriasObjetivo(meta);
        } else {
            // Calcular meta baseada nas calorias totais das refeições
            int meta = switch (plano.getObjetivo()) {
                case Cutting -> (int)(total * 0.80);
                case Bulking -> (int)(total * 1.15);
            };
            System.out.println("[CICLO_OBJETIVO] Calculando meta baseada em calorias totais: " + meta);
            plano.definirCaloriasObjetivo(meta);
        }
        System.out.println("[CICLO_OBJETIVO] Meta ajustada para: " + plano.getCaloriasObjetivo());
    }
}
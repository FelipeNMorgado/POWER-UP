package Up.Power.aplicacao.planoNutricional.commands;

import Up.Power.planoNutricional.Objetivo;
import java.util.List;

public record ModificarPlanoNutricionalCommand(
        int planoId,
        Objetivo objetivo,
        List<Integer> refeicoesIds
) {}

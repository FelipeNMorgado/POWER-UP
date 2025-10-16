package Up.Power.planoNutricional;

import Up.Power.PlanoNutricional;

public interface PlanoNutricionalRepository {
    void salvar(PlanoNutricional planoN);
    PlanoNutricional obter(PlanoNId plano);
}



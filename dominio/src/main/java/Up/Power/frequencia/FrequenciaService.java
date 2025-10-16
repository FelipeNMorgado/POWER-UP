package Up.Power.frequencia;

import Up.Power.Frequencia;
import Up.Power.perfil.PerfilId;

import java.util.List;
import java.util.Date;

public interface FrequenciaService {
    List<Frequencia> listarFrequencias(PerfilId perfil, FrequenciaId frequencia);
    int calcularFrequenciaCorrida(PerfilId perfil, Date inicio, Date fim);
}



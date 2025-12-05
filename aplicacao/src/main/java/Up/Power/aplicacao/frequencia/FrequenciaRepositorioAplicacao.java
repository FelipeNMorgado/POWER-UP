package Up.Power.aplicacao.frequencia;

import java.util.List;

public interface FrequenciaRepositorioAplicacao {
    List<FrequenciaResumo> listarPorPerfil(Integer perfilId);
}

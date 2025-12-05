package Up.Power.frequencia;

import Up.Power.Frequencia;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface FrequenciaRepository {
    void salvar(Frequencia frequencia);
    Frequencia obterFrequencia(FrequenciaId frequencia, LocalDateTime atual);

    List<Frequencia> listarPorPerfil(Integer perfilId);
}



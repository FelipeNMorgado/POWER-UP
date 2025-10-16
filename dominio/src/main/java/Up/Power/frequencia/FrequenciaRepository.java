package Up.Power.frequencia;

import Up.Power.Frequencia;

import java.time.LocalDateTime;

public interface FrequenciaRepository {
    void salvar(Frequencia frequencia);
    Frequencia obterFrequencia(FrequenciaId frequencia, LocalDateTime atual);
}



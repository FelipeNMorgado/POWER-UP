package Up.Power.frequencia;

import Up.Power.Frequencia;
import Up.Power.perfil.PerfilId;
import Up.Power.treino.TreinoId;

import java.time.LocalDateTime;
import java.util.Optional;

public class FrequenciaService {

    private final FrequenciaRepository repository;
    private int contagemSemanal = 0;

    public FrequenciaService(FrequenciaRepository repository) {
        this.repository = repository;
    }

    // 🏋️ Registra presença (demarca treino)
    public void registrarPresenca(FrequenciaId frequenciaId, PerfilId perfilId, TreinoId treinoId) {
        Frequencia frequencia = new Frequencia(frequenciaId, perfilId, treinoId, LocalDateTime.now());
        repository.salvar(frequencia);
        contagemSemanal++;
    }

    public void registrarAusencia(FrequenciaId frequenciaId) {
        Frequencia freq = repository.obterFrequencia(frequenciaId, LocalDateTime.now());
        if (freq != null) {
        }
        contagemSemanal = 0;
    }

    public boolean usuarioFoiAoTreino(FrequenciaId frequenciaId) {
        return Optional.ofNullable(repository.obterFrequencia(frequenciaId, LocalDateTime.now()))
                .isPresent();
    }

    public int getContagemSemanal() {
        return contagemSemanal;
    }

    public void resetarContagemSemanal() {
        contagemSemanal = 0;
    }
}

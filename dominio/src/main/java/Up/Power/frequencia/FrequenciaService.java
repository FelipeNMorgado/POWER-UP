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

    public void registrarPresenca(FrequenciaId frequenciaId, PerfilId perfilId, TreinoId treinoId) {
        registrarPresencaComFoto(frequenciaId, perfilId, treinoId, null);
    }

    public void registrarPresencaComFoto(FrequenciaId frequenciaId, PerfilId perfilId, TreinoId treinoId, String fotoBase64) {
        Frequencia frequencia = new Frequencia(frequenciaId, perfilId, treinoId, LocalDateTime.now());

        // Se houver foto, adiciona
        if (fotoBase64 != null && !fotoBase64.isBlank()) {
            frequencia.setFoto(fotoBase64);
            System.out.println("Foto adicionada à frequência.");
        } else {
            System.out.println("Nenhuma foto enviada, presença registrada normalmente.");
        }

        repository.salvar(frequencia);
        contagemSemanal++;
    }

    public void registrarAusencia(FrequenciaId frequenciaId) {
        Frequencia freq = repository.obterFrequencia(frequenciaId, LocalDateTime.now());
        if (freq != null) {
            // Poderia registrar a ausência explicitamente aqui, se quiser no futuro.
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

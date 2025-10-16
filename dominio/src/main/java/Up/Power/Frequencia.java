package Up.Power;

import Up.Power.frequencia.FrequenciaId;
import Up.Power.perfil.PerfilId;

import java.time.LocalDateTime;

public class Frequencia {
    private FrequenciaId id;
    private PerfilId perfil;
    private TreinoId treino;
    private LocalDateTime dataDePresenca;
    private String foto;

    public Frequencia(FrequenciaId id, PerfilId perfil, TreinoId treino, LocalDateTime dataDePresenca) {
        this.id = id;
        this.perfil = perfil;
        this.treino = treino;
        this.dataDePresenca = dataDePresenca;
    }

    public FrequenciaId getId() { return id; }
    public PerfilId getPerfil() { return perfil; }
    public TreinoId getTreino() { return treino; }
    public LocalDateTime getDataDePresenca() { return dataDePresenca; }
    public String getFoto() { return foto; }

    public void setFoto(String foto) { this.foto = foto; }
}



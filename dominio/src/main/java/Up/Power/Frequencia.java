package Up.Power;

import Up.Power.frequencia.FrequenciaId;
import Up.Power.perfil.PerfilId;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TreinoId;

import java.time.LocalDateTime;

public class Frequencia {
    private FrequenciaId id;
    private PerfilId perfil;
    private TreinoId treino;
    private LocalDateTime dataDePresenca;
    private PlanoTId planoTreino;
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
    public PlanoTId getPlanoTId() { return planoTreino; }

    public void setFoto(String foto) { this.foto = foto; }
    public void setPlanoT(PlanoTId planoTId) { this.planoTreino = planoTreino; }
}



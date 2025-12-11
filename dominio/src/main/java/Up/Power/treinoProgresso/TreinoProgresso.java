package Up.Power.treinoProgresso;

import Up.Power.perfil.PerfilId;
import Up.Power.exercicio.ExercicioId;

import java.util.Date;

public class TreinoProgresso {
    private TreinoProgressoId id;
    private PerfilId perfilId;
    private ExercicioId exercicioId;
    private Date dataRegistro;
    private Double pesoKg;
    private Integer repeticoes;
    private Integer series;
    private Date createdAt;

    public TreinoProgresso(TreinoProgressoId id,
                           PerfilId perfilId,
                           ExercicioId exercicioId,
                           Date dataRegistro,
                           Double pesoKg,
                           Integer repeticoes,
                           Integer series,
                           Date createdAt) {
        this.id = id;
        this.perfilId = perfilId;
        this.exercicioId = exercicioId;
        this.dataRegistro = dataRegistro;
        this.pesoKg = pesoKg;
        this.repeticoes = repeticoes;
        this.series = series;
        this.createdAt = createdAt;
    }

    public TreinoProgressoId getId() {
        return id;
    }

    public PerfilId getPerfilId() {
        return perfilId;
    }

    public ExercicioId getExercicioId() {
        return exercicioId;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public Integer getSeries() {
        return series;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(TreinoProgressoId id) {
        this.id = id;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }
}


package Up.Power.infraestrutura.persistencia.jpa.treinoProgresso;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "treino_progresso")
public class TreinoProgressoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    @Column(name = "exercicio_id", nullable = false)
    private Integer exercicioId;

    @Column(name = "data_registro", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataRegistro;

    @Column(name = "peso_kg")
    private Double pesoKg;

    @Column(name = "repeticoes")
    private Integer repeticoes;

    @Column(name = "series")
    private Integer series;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public TreinoProgressoJpa() {}

    public TreinoProgressoJpa(Integer id, Integer perfilId, Integer exercicioId, Date dataRegistro, Double pesoKg, Integer repeticoes, Integer series, Date createdAt) {
        this.id = id;
        this.perfilId = perfilId;
        this.exercicioId = exercicioId;
        this.dataRegistro = dataRegistro;
        this.pesoKg = pesoKg;
        this.repeticoes = repeticoes;
        this.series = series;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public Integer getPerfilId() { return perfilId; }
    public Integer getExercicioId() { return exercicioId; }
    public Date getDataRegistro() { return dataRegistro; }
    public Double getPesoKg() { return pesoKg; }
    public Integer getRepeticoes() { return repeticoes; }
    public Integer getSeries() { return series; }
    public Date getCreatedAt() { return createdAt; }

    public void setId(Integer id) { this.id = id; }
    public void setPerfilId(Integer perfilId) { this.perfilId = perfilId; }
    public void setExercicioId(Integer exercicioId) { this.exercicioId = exercicioId; }
    public void setDataRegistro(Date dataRegistro) { this.dataRegistro = dataRegistro; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }
    public void setRepeticoes(Integer repeticoes) { this.repeticoes = repeticoes; }
    public void setSeries(Integer series) { this.series = series; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}


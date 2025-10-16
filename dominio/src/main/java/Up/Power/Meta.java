package Up.Power;

import Up.Power.exercicio.ExercicioId;

import java.util.Date;

public class Meta {

    private final MetaId id;
    private ExercicioId exercicio;
    private TreinoId treino;
    private String nome;
    private Date fim;
    private Date inicio;

    public Meta(MetaId id, ExercicioId exercicio, TreinoId treino, String nome, Date fim, Date inicio) {
        this.id = id;
        this.exercicio = exercicio;
        this.treino = treino;
        this.nome = nome;
        this.fim = fim;
        this.inicio = inicio;
    }

    public MetaId getId() {
        return id;
    }

    public ExercicioId getExercicio() {
        return exercicio;
    }

    public TreinoId getTreino() {
        return treino;
    }

    public String getNome() {
        return nome;
    }

    public Date getInicio() {
        return inicio;
    }

    public Date getFim() {
        return fim;
    }
}

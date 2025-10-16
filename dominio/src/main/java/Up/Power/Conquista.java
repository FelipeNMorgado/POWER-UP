package Up.Power;

import Up.Power.conquista.ConquistaId;
import Up.Power.exercicio.ExercicioId;

public class Conquista {
    private ConquistaId id;
    private ExercicioId exercicio;
    private TreinoId treino;
    private String descricao;
    private String nome;

    public Conquista(ConquistaId id, ExercicioId exercicio, TreinoId treino, String descricao, String nome) {
        this.id = id;
        this.exercicio = exercicio;
        this.treino = treino;
        this.descricao = descricao;
        this.nome = nome;
    }

    public ConquistaId getId() { return id; }
    public ExercicioId getExercicio() { return exercicio; }
    public TreinoId getTreino() { return treino; }
    public String getDescricao() { return descricao; }
    public String getNome() { return nome; }
}



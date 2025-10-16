package Up.Power;

import java.time.LocalDateTime;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;

public class Treino {
    private final TreinoId id;
    private ExercicioId exercicio;
    private TipoTreino tipo;
    private LocalDateTime tempo;
    private float distancia;
    private int repeticoes;
    private float peso;
    private int series;

    public Treino(TreinoId id, ExercicioId exercicio, TipoTreino tipo) {
        this.id = id;
        this.exercicio = exercicio;
        this.tipo = tipo;
    }

    public Treino(TreinoId id, ExercicioId exercicio, TipoTreino tipo, int repeticoes, float peso, int series, int descanso) {
        this.id = id;
        this.exercicio = exercicio;
        this.tipo = tipo;
        this.repeticoes = repeticoes;
        this.peso = peso;
        this.series = series;
    }

    public void atualizarParametros(int repeticoes, float peso, int series, int descanso) {
        this.repeticoes = repeticoes;
        this.peso = peso;
        this.series = series;
    }

    public void atualizarExercicio(ExercicioId novoExercicio) {
        this.exercicio = novoExercicio;
    }

    // Getters
    public TreinoId getId() { return id; }
    public ExercicioId getExercicio() { return exercicio; }
    public TipoTreino getTipo() { return tipo; }
    public LocalDateTime getTempo() { return tempo; }
    public float getDistancia() { return distancia; }
    public int getRepeticoes() { return repeticoes; }
    public float getPeso() { return peso; }
    public int getSeries() { return series; }

    // Setters
    public void setTempo(LocalDateTime tempo) { this.tempo = tempo; }
    public void setDistancia(float distancia) { this.distancia = distancia; }
    public void setRepeticoes(int repeticoes) { this.repeticoes = repeticoes; }
    public void setPeso(float peso) { this.peso = peso; }
    public void setSeries(int series) { this.series = series; }
}

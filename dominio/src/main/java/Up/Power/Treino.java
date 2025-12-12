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
    private float recordeCarga = 0f;

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

    /**
     * Inicializa/atualiza o recorde deste treino com o seu próprio peso atual.
     * @return true se estabeleceu um novo recorde (peso > recorde anterior)
     */
    public boolean atualizarRecorde() {
        if (this.peso > this.recordeCarga) {
            this.recordeCarga = this.peso;
            System.out.println("🏆 Novo recorde inicializado: " + recordeCarga + " kg no exercício " + exercicio);
            return true;
        }
        return false;
    }

    /**
     * Compara o peso do treino 'novo' com o recorde deste treino e atualiza se for maior.
     * @param novo treino com peso a ser comparado
     * @return true se houve novo recorde
     */
    public boolean atualizarRecorde(Treino novo) {
        if (novo == null) return false;
        if (novo.getPeso() > this.recordeCarga) {
            this.recordeCarga = novo.getPeso();
            System.out.println("🏆 Novo recorde! " + recordeCarga + " kg no exercício " + novo.getExercicio());
            return true;
        }
        return false;
    }

    /**
     * Verifica se o treino atual mostra progresso em relação ao anterior.
     */
    public boolean estaProgredindo(Treino anterior) {
        return anterior != null &&
                this.exercicio != null &&
                this.exercicio.equals(anterior.exercicio) &&
                this.peso > anterior.peso;
    }

    // Getters
    public float getRecordeCarga() { return recordeCarga; }
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
    public void setRecordeCarga(float recordeCarga) { this.recordeCarga = recordeCarga; }

    /**
     * Extrai a duração em minutos de um LocalDateTime, ignorando a data.
     * Útil para comparar durações de treinos cardio armazenados com data base "1970-01-01".
     * 
     * @param tempo LocalDateTime contendo a duração (a data é ignorada)
     * @return Duração em minutos (horas * 60 + minutos + segundos/60)
     */
    public static long getDuracaoEmMinutos(LocalDateTime tempo) {
        if (tempo == null) return 0;
        // Extrai apenas a parte de tempo, ignorando a data
        return tempo.getHour() * 60L + tempo.getMinute() + tempo.getSecond() / 60L;
    }

    /**
     * Extrai a duração em segundos de um LocalDateTime, ignorando a data.
     * Útil para comparações precisas de duração de treinos cardio.
     * 
     * @param tempo LocalDateTime contendo a duração (a data é ignorada)
     * @return Duração em segundos (horas * 3600 + minutos * 60 + segundos)
     */
    public static long getDuracaoEmSegundos(LocalDateTime tempo) {
        if (tempo == null) return 0;
        // Extrai apenas a parte de tempo, ignorando a data
        return tempo.getHour() * 3600L + tempo.getMinute() * 60L + tempo.getSecond();
    }
}

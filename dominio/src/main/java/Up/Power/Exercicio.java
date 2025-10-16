package Up.Power;

import Up.Power.exercicio.ExercicioId;

public class Exercicio {
    private final ExercicioId id;
    private String nome;

    public Exercicio(ExercicioId id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ExercicioId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

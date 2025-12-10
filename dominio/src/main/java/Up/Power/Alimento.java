package Up.Power;

import Up.Power.alimento.AlimentoId;
import Up.Power.alimento.Categoria;

public class Alimento {
    private AlimentoId id;
    private Categoria categoria;
    private String nome;
    private int calorias;
    private float gramas;

    public Alimento(AlimentoId id, Categoria categoria, String nome, int calorias, float gramas) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.calorias = calorias;
        this.gramas = gramas;
    }

    public AlimentoId getAlimento() { return id; }
    public Categoria getCategoria() { return categoria; }
    public String getNome() { return nome; }
    public int getCalorias() { return calorias; }
    public float getGramas() { return gramas; }
}



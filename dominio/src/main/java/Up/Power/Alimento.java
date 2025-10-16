package Up.Power;

import Up.Power.alimento.AlimentoId;

public class Alimento {
    private AlimentoId alimento;
    private Categoria categoria;
    private String nome;
    private int calorias;
    private float quantidade;

    public Alimento(AlimentoId alimento, Categoria categoria, String nome, int calorias, float quantidade) {
        this.alimento = alimento;
        this.categoria = categoria;
        this.nome = nome;
        this.calorias = calorias;
        this.quantidade = quantidade;
    }

    public AlimentoId getAlimento() { return alimento; }
    public Categoria getCategoria() { return categoria; }
    public String getNome() { return nome; }
    public int getCalorias() { return calorias; }
    public float getQuantidade() { return quantidade; }
}



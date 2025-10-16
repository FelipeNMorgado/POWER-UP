package Up.Power;

import Up.Power.acessorio.AcessorioId;

public class Acessorio {
    private AcessorioId id;
    private String icone;
    private int preco;
    private String nome;

    public Acessorio(AcessorioId id, String icone, int preco, String nome) {
        this.id = id;
        this.icone = icone;
        this.preco = preco;
        this.nome = nome;
    }

    public AcessorioId getId() { return id; }
    public String getIcone() { return icone; }
    public int getPreco() { return preco; }
    public String getNome() { return nome; }
}



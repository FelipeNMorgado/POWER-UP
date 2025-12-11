package Up.Power;

import Up.Power.acessorio.AcessorioId;

public class Acessorio {
    private AcessorioId id;
    private String icone;
    private int preco;
    private String nome;
    private String imagem;
    private String qualidade;   // Ex.: Basica, Esportiva, Premium
    private String categoria;   // Ex.: Roupas, Acessorios
    private String subcategoria; // Ex.: Regatas, Camisetas, Colar, Straps

    public Acessorio(
            AcessorioId id,
            String icone,
            int preco,
            String nome,
            String imagem,
            String qualidade,
            String categoria,
            String subcategoria) {
        this.id = id;
        this.icone = icone;
        this.preco = preco;
        this.nome = nome;
        this.imagem = imagem;
        this.qualidade = qualidade;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
    }

    public AcessorioId getId() { return id; }
    public String getIcone() { return icone; }
    public int getPreco() { return preco; }
    public String getNome() { return nome; }
    public String getImagem() {return imagem;}
    public String getQualidade() { return qualidade; }
    public String getCategoria() { return categoria; }
    public String getSubcategoria() { return subcategoria; }
}



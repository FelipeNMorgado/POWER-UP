package Up.Power;

import Up.Power.conquista.ConquistaId;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

public class Conquista {
    private ConquistaId id;
    private ExercicioId exercicio;
    private TreinoId treino;
    private String descricao;
    private String nome;
    
    // Critérios para conquistar automaticamente
    private Float pesoMinimo; // Peso mínimo em kg para exercícios de força
    private Integer atributoMinimo; // Atributo mínimo do avatar (força, resistência, etc.)
    private String tipoAtributo; // "forca", "resistencia", "agilidade", "nivel"
    private Integer repeticoesMinimas; // Número mínimo de repetições
    private Integer seriesMinimas; // Número mínimo de séries

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
    
    // Getters para critérios
    public Float getPesoMinimo() { return pesoMinimo; }
    public Integer getAtributoMinimo() { return atributoMinimo; }
    public String getTipoAtributo() { return tipoAtributo; }
    public Integer getRepeticoesMinimas() { return repeticoesMinimas; }
    public Integer getSeriesMinimas() { return seriesMinimas; }
    
    // Setters para critérios
    public void setPesoMinimo(Float pesoMinimo) { this.pesoMinimo = pesoMinimo; }
    public void setAtributoMinimo(Integer atributoMinimo) { this.atributoMinimo = atributoMinimo; }
    public void setTipoAtributo(String tipoAtributo) { this.tipoAtributo = tipoAtributo; }
    public void setRepeticoesMinimas(Integer repeticoesMinimas) { this.repeticoesMinimas = repeticoesMinimas; }
    public void setSeriesMinimas(Integer seriesMinimas) { this.seriesMinimas = seriesMinimas; }
}



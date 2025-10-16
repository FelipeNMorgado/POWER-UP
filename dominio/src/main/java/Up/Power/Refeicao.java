package Up.Power;

import Up.Power.alimento.AlimentoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Refeicao {
    private RefeicaoId id;
    private TipoRefeicao tipo;
    private List<AlimentoId> alimentos;
    private int caloriasTotais;
    private Date inicio;
    private Date fim;

    public Refeicao(RefeicaoId id, TipoRefeicao tipo) {
        this.id = id;
        this.tipo = tipo;
        this.alimentos = new ArrayList<>();
    }

    public void adicionarAlimento(AlimentoId alimento) { this.alimentos.add(alimento); }

    public RefeicaoId getId() { return id; }
    public TipoRefeicao getTipo() { return tipo; }
    public List<AlimentoId> getAlimentos() { return alimentos; }
    public int getCaloriasTotais() { return caloriasTotais; }
    public Date getInicio() { return inicio; }
    public Date getFim() { return fim; }

    public void setCaloriasTotais(int caloriasTotais) { this.caloriasTotais = caloriasTotais; }
    public void setInicio(Date inicio) { this.inicio = inicio; }
    public void setFim(Date fim) { this.fim = fim; }
}

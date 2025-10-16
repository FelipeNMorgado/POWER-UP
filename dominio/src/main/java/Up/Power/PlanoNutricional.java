package Up.Power;

import Up.Power.planoNutricional.Objetivo;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.refeicao.RefeicaoId;

import java.util.ArrayList;
import java.util.List;

public class PlanoNutricional {
    private EstadoPlano estado;
    private PlanoNId id;
    private Objetivo objetivo;
    private List<RefeicaoId> refeicoes;
    private int caloriasTotais;
    private int caloriasObjetivo;

    public PlanoNutricional(PlanoNId id, Objetivo objetivo) {
        this.id = id;
        this.objetivo = objetivo;
        this.estado = EstadoPlano.Ativo;
        this.refeicoes = new ArrayList<>();
    }

    public void adicionarRefeicao(RefeicaoId refeicaoId) { this.refeicoes.add(refeicaoId); }

    public PlanoNId getId() { return id; }
    public Objetivo getObjetivo() { return objetivo; }
    public List<RefeicaoId> getRefeicoes() { return refeicoes; }
    public int getCaloriasTotais() { return caloriasTotais; }
    public int getCaloriasObjetivo() { return caloriasObjetivo; }
}



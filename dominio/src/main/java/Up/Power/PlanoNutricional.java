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
    private Email usuarioEmail;

    public PlanoNutricional(PlanoNId id, Objetivo objetivo, Email usuarioEmail) {
        this.id = id;
        this.objetivo = objetivo;
        this.estado = EstadoPlano.Ativo;
        // IMPORTANTE: Sempre criar uma nova lista vazia para garantir isolamento
        this.refeicoes = new ArrayList<>();
        this.usuarioEmail = usuarioEmail;
    }
    
    // Método para limpar todas as refeições (garantir que a lista está vazia)
    public void limparRefeicoes() {
        if (this.refeicoes != null) {
            this.refeicoes.clear();
        } else {
            this.refeicoes = new ArrayList<>();
        }
    }

    public void adicionarRefeicao(RefeicaoId refeicaoId) { this.refeicoes.add(refeicaoId); }

    public PlanoNId getId() { return id; }
    public Objetivo getObjetivo() { return objetivo; }
    public List<RefeicaoId> getRefeicoes() { return refeicoes; }
    public int getCaloriasTotais() { return caloriasTotais; }
    public int getCaloriasObjetivo() { return caloriasObjetivo; }
    public Email getUsuarioEmail() { return usuarioEmail; }

    public void setId(PlanoNId id) { this.id = id; }

    public void definirCaloriasObjetivo(int calorias) {
        this.caloriasObjetivo = calorias;
    }

    public void adicionarRefeicaoList(List<RefeicaoId> refeicoes) {
        // IMPORTANTE: Criar uma cópia da lista para evitar compartilhamento de referência
        if (refeicoes != null) {
            this.refeicoes.addAll(new ArrayList<>(refeicoes));
        }
    }

    public void definirCaloriasTotais(int calorias) {
        this.caloriasTotais = calorias;
    }

}



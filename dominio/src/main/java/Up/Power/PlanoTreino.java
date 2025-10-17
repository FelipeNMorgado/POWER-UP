package Up.Power;

import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;

import java.util.ArrayList;
import java.util.List;

public class PlanoTreino {
    private PlanoTId id;
    private final Email usuarioEmail;
    private EstadoPlano estado;
    private List<Treino> treinos;
    private List<Dias> dias;
    private String nome;
    private float recordeCarga = 0f;

    public PlanoTreino(PlanoTId id, Email usuarioEmail, String nome) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.nome = nome;
        this.estado = EstadoPlano.Ativo;
        this.treinos = new ArrayList<>();
        this.dias = new ArrayList<>();
    }

    public void adicionarTreino(Treino treino) {
        this.treinos.add(treino);
    }

    public void removerTreino(Treino treino) {
        this.treinos.remove(treino);
    }

    public void adicionarDia(Dias dia) {
        this.dias.add(dia);
    }

    public void alterarEstadoPlano(EstadoPlano estado) {
        this.estado = estado;
    }

    public boolean temExercicios() {
        return !treinos.isEmpty();
    }

    public void atualizarTreino(Treino treinoAntigo, Treino treinoNovo) {
        int index = treinos.indexOf(treinoAntigo);
        if (index != -1) {
            treinos.set(index, treinoNovo);
        }
    }

    public boolean estaProgredindo() {
        if (treinos.size() < 2) return false;

        Treino ultimo = treinos.get(treinos.size() - 1);
        Treino anterior = treinos.get(treinos.size() - 2);

        // verifica se é o mesmo exercício e o peso aumentou
        return ultimo.getExercicio().equals(anterior.getExercicio()) &&
                ultimo.getPeso() > anterior.getPeso();
    }

    public boolean atualizarRecorde(Treino treino) {
        if (treino.getPeso() > recordeCarga) {
            recordeCarga = treino.getPeso();
            System.out.println("🏆 Novo recorde! " + recordeCarga + " kg no exercício " + treino.getExercicio());
            return true;
        }
        return false;
    }


    // Getters
    public float getRecordeCarga() {
        return recordeCarga;
    }
    public PlanoTId getId() { return id; }
    public Email getUsuarioEmail() { return usuarioEmail; }
    public EstadoPlano getEstadoPlano() { return estado; }
    public List<Treino> getTreinos() { return treinos; }
    public List<Dias> getDias() { return dias; }
    public String getNome() { return nome; }

    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setTreinos(List<Treino> treinos) { this.treinos = treinos; }
    public void setDias(List<Dias> dias) { this.dias = dias; }
}

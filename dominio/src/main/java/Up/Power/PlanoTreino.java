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
        if (treino == null || treino.getId() == null) {
            return;
        }
        // Remove por ID para garantir que funciona mesmo se os objetos não forem iguais
        this.treinos.removeIf(t -> t.getId() != null && t.getId().equals(treino.getId()));
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

        return ultimo.estaProgredindo(anterior);
    }

    /**
     * Atualiza o recorde baseado no treino informado.
     * Procura um treino existente do mesmo exercício e delega a atualização.
     * Se não houver treino existente, adiciona o treino e inicializa seu recorde.
     *
     * @param treino treino novo a ser considerado
     * @return true se houve novo recorde
     */
    public boolean atualizarRecorde(Treino treino) {
        if (treino == null) return false;

        // procura um treino existente do mesmo exercício (qualquer um)
        for (Treino t : treinos) {
            if (t.getExercicio() != null && t.getExercicio().equals(treino.getExercicio())) {
                // delega a atualização ao treino encontrado
                return t.atualizarRecorde(treino);
            }
        }

        // se não encontrou treino do mesmo exercício, adiciona e inicializa o recorde
        this.adicionarTreino(treino);
        return treino.atualizarRecorde();
    }

    public float getRecordeCarga() {
        return treinos.stream()
                .map(Treino::getRecordeCarga)
                .max(Float::compare)
                .orElse(0f);
    }

    // Getters e Setters
    public PlanoTId getId() { return id; }
    public Email getUsuarioEmail() { return usuarioEmail; }
    public EstadoPlano getEstadoPlano() { return estado; }
    public List<Treino> getTreinos() { return treinos; }
    public List<Dias> getDias() { return dias; }
    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }
    public void setTreinos(List<Treino> treinos) { this.treinos = treinos; }
    public void setDias(List<Dias> dias) { this.dias = dias; }
    public void setId(PlanoTId id) { this.id = id; }
}

// Up/Power/aplicacao/eventos/TreinoConcluidoEvent.java

package Up.Power.aplicacao.eventos; // Pacote sugerido

import Up.Power.Treino; // Assumindo que a classe Treino está em Up.Power

public class TreinoConcluidoEvent {

    private final Integer perfilId;
    private final Treino treino;

    public TreinoConcluidoEvent(Integer perfilId, Treino treino) {
        this.perfilId = perfilId;
        this.treino = treino;
    }

    // O GETTER NECESSÁRIO!
    public Integer getPerfilId() {
        return perfilId;
    }

    // O GETTER NECESSÁRIO!
    public Treino getTreino() {
        return treino;
    }
}
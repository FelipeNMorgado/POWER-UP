// Up/Power/aplicacao/eventos/AtributosAtualizadosEvent.java

package Up.Power.aplicacao.eventos; // Pacote sugerido

import Up.Power.Avatar; // Assumindo que a classe Avatar está em Up.Power

public class AtributosAtualizadosEvent {

    private final Integer perfilId;
    private final Avatar avatar;

    public AtributosAtualizadosEvent(Integer perfilId, Avatar avatar) {
        this.perfilId = perfilId;
        this.avatar = avatar;
    }

    // O GETTER NECESSÁRIO!
    public Integer getPerfilId() {
        return perfilId;
    }

    // O GETTER NECESSÁRIO!
    public Avatar getAvatar() {
        return avatar;
    }
}
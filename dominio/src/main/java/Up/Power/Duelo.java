package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.duelo.DueloId;

public class Duelo {
    private DueloId id;
    private AvatarId avatar1;
    private AvatarId avatar2;
    private String resultado;

    public Duelo(DueloId id, AvatarId avatar1, AvatarId avatar2) {
        this.id = id;
        this.avatar1 = avatar1;
        this.avatar2 = avatar2;
    }

    public DueloId getId() { return id; }
    public AvatarId getAvatar1() { return avatar1; }
    public AvatarId getAvatar2() { return avatar2; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}



package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.duelo.DueloId;

import java.time.LocalDateTime;

public class Duelo {
    private DueloId id;
    private AvatarId avatar1;
    private AvatarId avatar2;
    private String resultado;
    private LocalDateTime dataDuelo;

    public Duelo(DueloId id, AvatarId avatar1, AvatarId avatar2, String resultado, LocalDateTime dataDuelo) {
        this.id = id;
        this.avatar1 = avatar1;
        this.avatar2 = avatar2;
        this.resultado = resultado;
        this.dataDuelo = dataDuelo;
    }

    public Duelo(AvatarId avatar1, AvatarId avatar2) {
        this.id = null; // Gerado pelo banco/mock
        this.avatar1 = avatar1;
        this.avatar2 = avatar2;
        this.dataDuelo = LocalDateTime.now(); // <-- Data é registrada na criação
    }

    public DueloId getId() { return id; }
    public AvatarId getAvatar1() { return avatar1; }
    public AvatarId getAvatar2() { return avatar2; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public LocalDateTime getDataDuelo() {
        return dataDuelo;
    }

    public void setId(DueloId id) {
        this.id = id;
    }

    public void setDataDuelo(LocalDateTime dataDuelo) {
        this.dataDuelo = dataDuelo;
    }
}



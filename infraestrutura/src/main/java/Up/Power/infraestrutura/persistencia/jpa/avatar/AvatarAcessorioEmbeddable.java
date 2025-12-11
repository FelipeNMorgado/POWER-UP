package Up.Power.infraestrutura.persistencia.jpa.avatar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AvatarAcessorioEmbeddable {

    @Column(name = "acessorio_id")
    private Integer acessorioId;

    @Column(name = "equipado")
    private Boolean equipado;

    public AvatarAcessorioEmbeddable() {}

    public AvatarAcessorioEmbeddable(Integer acessorioId, Boolean equipado) {
        this.acessorioId = acessorioId;
        this.equipado = equipado;
    }

    public Integer getAcessorioId() {
        return acessorioId;
    }

    public void setAcessorioId(Integer acessorioId) {
        this.acessorioId = acessorioId;
    }

    public Boolean getEquipado() {
        return equipado;
    }

    public void setEquipado(Boolean equipado) {
        this.equipado = equipado;
    }
}


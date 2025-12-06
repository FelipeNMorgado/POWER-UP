package Up.Power.equipe;

import java.util.Objects;

public class EquipeId {
    private int id;

    public EquipeId() {
        // Construtor padrão para compatibilidade
    }

    public EquipeId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EquipeId equipeId = (EquipeId) o;
        return id == equipeId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}

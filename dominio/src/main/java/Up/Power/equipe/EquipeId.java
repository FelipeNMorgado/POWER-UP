package Up.Power.equipe;

import java.util.Objects;

public class EquipeId {
    private int id;

    public int getId() {
        return id;
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

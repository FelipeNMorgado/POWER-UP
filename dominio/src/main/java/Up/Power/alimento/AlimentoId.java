package Up.Power.alimento;

import java.util.Objects;

public class AlimentoId {
    private int id;

    public AlimentoId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlimentoId alimentoId = (AlimentoId) o;
        return id == alimentoId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



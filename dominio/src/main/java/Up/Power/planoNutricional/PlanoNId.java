package Up.Power.planoNutricional;

import java.util.Objects;

public class PlanoNId {
    private int id;

    public PlanoNId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanoNId planoNId = (PlanoNId) o;
        return id == planoNId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



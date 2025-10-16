package Up.Power.duelo;

import java.util.Objects;

public class DueloId {
    private int id;

    public DueloId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DueloId dueloId = (DueloId) o;
        return id == dueloId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



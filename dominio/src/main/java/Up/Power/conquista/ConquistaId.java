package Up.Power.conquista;

import java.util.Objects;

public class ConquistaId {
    private int id;

    public ConquistaId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConquistaId conquistaId = (ConquistaId) o;
        return id == conquistaId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



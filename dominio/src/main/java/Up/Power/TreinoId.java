package Up.Power;

import java.util.Objects;

public class TreinoId {
    private int id;

    public TreinoId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TreinoId treinoId = (TreinoId) o;
        return id == treinoId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

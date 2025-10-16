package Up.Power;

import java.util.Objects;

public class RefeicaoId {
    private int id;

    public RefeicaoId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefeicaoId refeicaoId = (RefeicaoId) o;
        return id == refeicaoId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}


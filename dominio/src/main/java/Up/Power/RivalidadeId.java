package Up.Power;

import java.util.Objects;

public class RivalidadeId {
    private final int id;

    public RivalidadeId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RivalidadeId rivalidadeId = (RivalidadeId) o;
        return id == rivalidadeId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



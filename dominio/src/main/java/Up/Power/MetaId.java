package Up.Power;

import java.util.Objects;

public final class MetaId {
    private final int id;

    public MetaId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MetaId metaId = (MetaId) o;
        return id == metaId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}

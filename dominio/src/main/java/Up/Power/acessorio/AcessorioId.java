package Up.Power.acessorio;

import java.util.Objects;

public class AcessorioId {
    private int id;

    public AcessorioId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AcessorioId acessorioId = (AcessorioId) o;
        return id == acessorioId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



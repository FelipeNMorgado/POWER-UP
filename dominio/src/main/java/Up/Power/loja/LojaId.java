package Up.Power.loja;

import java.util.Objects;

public class LojaId {
    private int id;

    public LojaId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LojaId lojaId = (LojaId) o;
        return id == lojaId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



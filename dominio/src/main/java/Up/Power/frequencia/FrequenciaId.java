package Up.Power.frequencia;

import java.util.Objects;

public class FrequenciaId {
    private int id;

    public FrequenciaId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FrequenciaId that = (FrequenciaId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}



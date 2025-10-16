package Up.Power;

import java.util.Objects;

public class PlanoTId {
    private int id;

    public PlanoTId(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanoTId planoTId = (PlanoTId) o;
        return id == planoTId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}

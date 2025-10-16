package Up.Power.perfil;

public class PerfilId {
    private int id;

    public PerfilId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PerfilId perfilId = (PerfilId) obj;
        return id == perfilId.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}



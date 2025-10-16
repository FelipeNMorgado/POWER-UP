package Up.Power;

public final class AmizadeId {
    private final int codigo;

    public AmizadeId(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AmizadeId that = (AmizadeId) obj;
        return codigo == that.codigo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codigo);
    }
}

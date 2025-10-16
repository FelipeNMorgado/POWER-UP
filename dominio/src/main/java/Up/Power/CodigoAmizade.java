package Up.Power;

public final class CodigoAmizade {
    private final int codigo;

    public CodigoAmizade(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CodigoAmizade that = (CodigoAmizade) obj;
        return codigo == that.codigo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codigo);
    }
}

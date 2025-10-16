package Up.Power;

public final class Email {
    private final String caracteres;

    public Email(String caracteres) {
        if (caracteres == null || caracteres.isBlank())
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        this.caracteres = caracteres;
    }

    public String getCaracteres() {
        return caracteres;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Email email = (Email) obj;
        return caracteres != null ? caracteres.equals(email.caracteres) : email.caracteres == null;
    }

    @Override
    public int hashCode() {
        return caracteres != null ? caracteres.hashCode() : 0;
    }

    @Override
    public String toString() {
        return caracteres;
    }
}

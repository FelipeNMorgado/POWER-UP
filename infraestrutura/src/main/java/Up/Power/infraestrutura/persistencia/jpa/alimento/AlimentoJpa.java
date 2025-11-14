package Up.Power.infraestrutura.persistencia.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import Up.Power.Alimento;
import Up.Power.alimento.AlimentoId;
import Up.Power.alimento.Categoria;

@Entity
@Table(name = "ALIMENTO")
public class AlimentoJpa {

    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String nome;
    private int calorias;
    private float quantidade;

    // 🔹 Construtor padrão exigido pelo JPA
    protected AlimentoJpa() {}

    // 🔹 Construtor completo
    public AlimentoJpa(int id, Categoria categoria, String nome, int calorias, float quantidade) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.calorias = calorias;
        this.quantidade = quantidade;
    }

    // =============================
    // 🔁 Conversões entre domínio e JPA
    // =============================

    // 🔹 De domínio → JPA
    public static AlimentoJpa fromDomain(Alimento alimento) {
        return new AlimentoJpa(
                Integer.parseInt(alimento.getAlimento().toString()),
                alimento.getCategoria(),
                alimento.getNome(),
                alimento.getCalorias(),
                alimento.getQuantidade()
        );
    }

    // 🔹 De JPA → domínio
    public Alimento toDomain() {
        return new Alimento(
                new AlimentoId(String.valueOf(id)),
                categoria,
                nome,
                calorias,
                quantidade
        );
    }

    @Override
    public String toString() {
        return nome + " (" + calorias + " kcal)";
    }
}

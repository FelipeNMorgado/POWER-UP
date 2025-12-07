package Up.Power.infraestrutura.persistencia.jpa.loja;

import Up.Power.Loja;
import Up.Power.acessorio.AcessorioId;
import Up.Power.loja.LojaId;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

// ===========================
// ======== ENTITY ===========
// ===========================

@Entity
@Table(name = "loja")
public class LojaJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    @CollectionTable(
            name = "loja_acessorios",
            joinColumns = @JoinColumn(name = "loja_id")
    )
    @Column(name = "acessorio_id")
    private List<Integer> acessoriosIds;

    public LojaJpa() {}

    public LojaJpa(Integer id, List<Integer> acessoriosIds) {
        this.id = id;
        this.acessoriosIds = acessoriosIds;
    }

    // Getters
    public Integer getId() { return id; }
    public List<Integer> getAcessoriosIds() { return acessoriosIds; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setAcessoriosIds(List<Integer> acessoriosIds) { this.acessoriosIds = acessoriosIds; }
}

// =====================================
// =========== JPA REPOSITORY ==========
// =====================================

interface JpaLojaRepository extends JpaRepository<LojaJpa, Integer> {
}

// =====================================
// =========== REPOSITORY IMPL =========
// =====================================
// Nota: Loja não tem repositório no domínio, então não implementamos aqui
// Se necessário no futuro, criar LojaRepository no domínio


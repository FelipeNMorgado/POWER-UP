package Up.Power.infraestrutura.persistencia.jpa.planoNutricional;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Up.Power.PlanoNutricional;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.planoNutricional.Objetivo;
import Up.Power.planoNutricional.PlanoNutricionalRepository;

@Entity
@Table(name = "plano_nutricional")
public class PlanoNutricionalJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Up.Power.planoNutricional.Objetivo objetivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Up.Power.EstadoPlano estado;

    @ElementCollection
    @CollectionTable(
            name = "plano_refeicoes",
            joinColumns = @JoinColumn(name = "plano_id")
    )
    @Column(name = "refeicao_id")
    private List<Integer> refeicoes = new ArrayList<>();

    private int caloriasTotais;
    private int caloriasObjetivo;

    public PlanoNutricionalJpa() {}

    public PlanoNutricionalJpa(Integer id,
                               Up.Power.planoNutricional.Objetivo objetivo,
                               Up.Power.EstadoPlano estado,
                               List<Integer> refeicoes,
                               int caloriasTotais,
                               int caloriasObjetivo) {
        this.id = id;
        this.objetivo = objetivo;
        this.estado = estado;
        this.refeicoes = refeicoes;
        this.caloriasTotais = caloriasTotais;
        this.caloriasObjetivo = caloriasObjetivo;
    }

    public Integer getId() { return id; }
    public Up.Power.planoNutricional.Objetivo getObjetivo() { return objetivo; }
    public Up.Power.EstadoPlano getEstado() { return estado; }
    public List<Integer> getRefeicoes() { return refeicoes; }
    public int getCaloriasTotais() { return caloriasTotais; }
    public int getCaloriasObjetivo() { return caloriasObjetivo; }

    public void setId(Integer id) { this.id = id; }
    public void setObjetivo(Up.Power.planoNutricional.Objetivo objetivo) { this.objetivo = objetivo; }
    public void setEstado(Up.Power.EstadoPlano estado) { this.estado = estado; }
    public void setRefeicoes(List<Integer> refeicoes) { this.refeicoes = refeicoes; }
    public void setCaloriasTotais(int caloriasTotais) { this.caloriasTotais = caloriasTotais; }
    public void setCaloriasObjetivo(int caloriasObjetivo) { this.caloriasObjetivo = caloriasObjetivo; }
}

@Repository
interface JpaPlanoNutricionalRepository extends JpaRepository<PlanoNutricionalJpa, Integer> {

}

@Repository
class PlanoNutricionalRepositoryImpl implements PlanoNutricionalRepository {

    private final JpaPlanoNutricionalRepository jpaRepository;
    private final PlanoNutricionalMapper mapper;

    public PlanoNutricionalRepositoryImpl(
            JpaPlanoNutricionalRepository jpaRepository,
            PlanoNutricionalMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(PlanoNutricional plano) {
        PlanoNutricionalJpa entity = mapper.toEntity(plano);
        jpaRepository.save(entity);
    }

    @Override
    public PlanoNutricional obter(PlanoNId id) {
        return jpaRepository.findById(id.getId())
                .map(mapper::toDomain)
                .orElse(null);
    }
}
package Up.Power.infraestrutura.persistencia.jpa.rivalidade;

import Up.Power.rivalidade.StatusRivalidade;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import Up.Power.Rivalidade;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeRepository;
import Up.Power.perfil.PerfilId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "rivalidade")
public class RivalidadeJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "perfil1_id", nullable = false)
    private Integer perfil1Id;

    @Column(name = "perfil2_id", nullable = false)
    private Integer perfil2Id;

    @Column(name = "exercicio_id", nullable = false)
    private Integer exercicioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusRivalidade status;

    @Column(name = "data_convite", nullable = false)
    private LocalDateTime dataConvite;

    @Column
    private LocalDateTime inicio;

    @Column
    private LocalDateTime fim;

    public Integer getId() {return id;}
    public Integer getPerfil1Id() {return perfil1Id;}
    public Integer getPerfil2Id() {return perfil2Id;}
    public Integer getExercicioId() {return exercicioId;}
    public StatusRivalidade getStatus() {return status;}
    public LocalDateTime getDataConvite() {return dataConvite;}
    public LocalDateTime getInicio() {return inicio;}
    public LocalDateTime getFim() {return fim;}
    public void setId(Integer id) {this.id = id;}
    public void setPerfil1Id(Integer perfil1Id) {this.perfil1Id = perfil1Id;}
    public void setPerfil2Id(Integer perfil2Id) {this.perfil2Id = perfil2Id;}
    public void setExercicioId(Integer exercicioId) {this.exercicioId = exercicioId;}
    public void setStatus(StatusRivalidade status) {this.status = status;}
    public void setDataConvite(LocalDateTime dataConvite) {this.dataConvite = dataConvite;}
    public void setInicio(LocalDateTime inicio) {this.inicio = inicio;}
    public void setFim(LocalDateTime fim) {this.fim = fim;}
}


@Repository
class RivalidadeRepositoryImpl implements RivalidadeRepository {

    private final JpaRivalidadeRepository jpa;
    private final RivalidadeMapper mapper;

    public RivalidadeRepositoryImpl(JpaRivalidadeRepository jpa, RivalidadeMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Rivalidade save(Rivalidade rivalidade) {
        RivalidadeJpa entity = mapper.toEntity(rivalidade);
        RivalidadeJpa saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Rivalidade> findById(RivalidadeId id) {
        return jpa.findById(id.getId()).map(mapper::toDomain);
    }

    @Override
    public boolean existsActiveRivalryForPerfil(PerfilId perfilId) {
        return jpa.existsActiveRivalryForPerfil(perfilId.getId());
    }
}



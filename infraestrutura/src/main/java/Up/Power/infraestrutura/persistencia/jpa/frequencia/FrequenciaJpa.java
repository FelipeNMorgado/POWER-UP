package Up.Power.infraestrutura.persistencia.jpa.frequencia;

import Up.Power.Frequencia;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.frequencia.FrequenciaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "frequencia")
public class FrequenciaJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    @Column(name = "treino_id", nullable = false)
    private Integer treinoId;

    @Column(name = "data_presenca", nullable = false)
    private LocalDateTime dataDePresenca;

    @Column(name = "plano_treino_id", nullable = false)
    private Integer planoTreinoId;

    @Column(name = "foto")
    private String foto;

    public Integer getId() { return id; }
    public Integer getPerfilId() { return perfilId; }
    public Integer getTreinoId() { return treinoId; }
    public LocalDateTime getDataDePresenca() { return dataDePresenca; }
    public Integer getPlanoTreinoId() { return planoTreinoId; }
    public String getFoto() { return foto; }

    public void setId(Integer id) { this.id = id; }
    public void setPerfilId(Integer perfilId) { this.perfilId = perfilId; }
    public void setTreinoId(Integer treinoId) { this.treinoId = treinoId; }
    public void setDataDePresenca(LocalDateTime dataDePresenca) { this.dataDePresenca = dataDePresenca; }
    public void setPlanoTreinoId(Integer planoTreinoId) { this.planoTreinoId = planoTreinoId; }
    public void setFoto(String foto) { this.foto = foto; }
}

interface JpaFrequenciaRepository extends JpaRepository<FrequenciaJpa, Integer> {

    @Query("SELECT f FROM FrequenciaJpa f WHERE f.id = :id AND f.dataDePresenca = :data")
    Optional<FrequenciaJpa> buscarPorData(Integer id, LocalDateTime data);
}

@Repository
class FrequenciaRepositoryImpl implements FrequenciaRepository {

    private final JpaFrequenciaRepository jpaRepo;
    private final FrequenciaMapper mapper;

    public FrequenciaRepositoryImpl(JpaFrequenciaRepository jpaRepo, FrequenciaMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Frequencia frequencia) {
        FrequenciaJpa entity = mapper.toEntity(frequencia);
        jpaRepo.save(entity);
    }

    @Override
    public Frequencia obterFrequencia(FrequenciaId id, LocalDateTime atual) {
        return jpaRepo.findById(id.getId())
                .map(mapper::toDomain)
                .orElse(null);
    }
}

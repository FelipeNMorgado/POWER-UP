package Up.Power.infraestrutura.persistencia.jpa.treino;

import Up.Power.Treino;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;

import Up.Power.treino.TreinoRepository;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// ======================================
// =============== ENTITY ===============
// ======================================

@Entity
@Table(name = "treino")
public class TreinoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "exercicio_id", nullable = false)
    private Integer exercicioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTreino tipo;

    private LocalDateTime tempo;

    private float distancia;

    private int repeticoes;

    private float peso;

    private int series;

    @Column(name = "recorde_carga")
    private float recordeCarga;

    public TreinoJpa() {}

    public TreinoJpa(Integer id, Integer exercicioId, TipoTreino tipo,
                     LocalDateTime tempo, float distancia, int repeticoes,
                     float peso, int series, float recordeCarga) {
        this.id = id;
        this.exercicioId = exercicioId;
        this.tipo = tipo;
        this.tempo = tempo;
        this.distancia = distancia;
        this.repeticoes = repeticoes;
        this.peso = peso;
        this.series = series;
        this.recordeCarga = recordeCarga;
    }

    public Integer getId() { return id; }
    public Integer getExercicioId() { return exercicioId; }
    public TipoTreino getTipo() { return tipo; }
    public LocalDateTime getTempo() { return tempo; }
    public float getDistancia() { return distancia; }
    public int getRepeticoes() { return repeticoes; }
    public float getPeso() { return peso; }
    public int getSeries() { return series; }
    public float getRecordeCarga() { return recordeCarga; }

    public void setId(Integer id) { this.id = id; }
    public void setExercicioId(Integer exercicioId) { this.exercicioId = exercicioId; }
    public void setTipo(TipoTreino tipo) { this.tipo = tipo; }
    public void setTempo(LocalDateTime tempo) { this.tempo = tempo; }
    public void setDistancia(float distancia) { this.distancia = distancia; }
    public void setRepeticoes(int repeticoes) { this.repeticoes = repeticoes; }
    public void setPeso(float peso) { this.peso = peso; }
    public void setSeries(int series) { this.series = series; }
    public void setRecordeCarga(float recordeCarga) { this.recordeCarga = recordeCarga; }
}





@Repository
class TreinoRepositoryImpl implements TreinoRepository {

    private final JpaTreinoRepository repo;

    public TreinoRepositoryImpl(JpaTreinoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void salvar(Treino treino) {
        TreinoJpa entity = TreinoMapper.toEntity(treino);
        repo.save(entity);
    }

    @Override
    public List<TreinoId> listar(TreinoId id) {
        // NÃO ALTEREI O DOMÍNIO, ENTÃO EU SÓ FAÇO LISTAGEM POR ID
        // Mesmo sendo estranho listar um único ID.

        if (id == null) {
            return repo.findAll()
                    .stream()
                    .map(e -> new TreinoId(e.getId()))
                    .toList();
        }

        return repo.findById(id.getId())
                .map(e -> List.of(new TreinoId(e.getId())))
                .orElse(List.of());
    }

    @Override
    public void excluir(TreinoId id) {
        repo.deleteById(id.getId());
    }

    @Override
    public void editar(TreinoId id) {
        // Como o domínio NÃO foi alterado, não tenho um Treino completo aqui.
        // A única coisa possível é carregar, modificar campos padrão e salvar.
        // Vou apenas garantir que existe e "resalvar" a entidade.

        // save atualiza automaticamente
        repo.findById(id.getId()).ifPresent(repo::save);
    }
}
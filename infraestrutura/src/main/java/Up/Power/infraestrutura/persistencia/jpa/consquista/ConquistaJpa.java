package Up.Power.infraestrutura.persistencia.jpa.consquista;

import Up.Power.conquista.ConquistaId;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Up.Power.Conquista;
import Up.Power.conquista.ConquistaRepository;
import java.util.Optional;

import java.util.List;

// ===========================
// ======== ENTITY ===========
// ===========================

    @Entity
    @Table(name = "conquista")
    public class ConquistaJpa {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "exercicio_id", nullable = false)
        private Integer exercicioId;

        @Column(name = "treino_id", nullable = false)
        private Integer treinoId;

        @Column(nullable = false)
        private String descricao;

        @Column(nullable = false)
        private String nome;

        @Column(nullable = false)
        private boolean concluida;

        public ConquistaJpa() {}

        public ConquistaJpa(Integer id, Integer exercicioId, Integer treinoId,
                            String descricao, String nome, boolean concluida) {
            this.id = id;
            this.exercicioId = exercicioId;
            this.treinoId = treinoId;
            this.descricao = descricao;
            this.nome = nome;
            this.concluida = concluida;
        }

        public Integer getId() { return id; }
        public Integer getExercicioId() { return exercicioId; }
        public Integer getTreinoId() { return treinoId; }
        public String getDescricao() { return descricao; }
        public String getNome() { return nome; }
        public boolean isConcluida() { return concluida; }

        public void setId(Integer id) { this.id = id; }
        public void setExercicioId(Integer exercicioId) { this.exercicioId = exercicioId; }
        public void setTreinoId(Integer treinoId) { this.treinoId = treinoId; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public void setNome(String nome) { this.nome = nome; }
        public void setConcluida(boolean concluida) { this.concluida = concluida; }
    }



    interface JpaConquistaRepository extends JpaRepository<ConquistaJpa, Integer> {

        List<ConquistaJpa> findByConcluidaFalse();

        List<ConquistaJpa> findByConcluidaTrue();
    }

    @Repository
    class ConquistaRepositoryImpl implements ConquistaRepository {

        private final JpaConquistaRepository repo;

        public ConquistaRepositoryImpl(JpaConquistaRepository repo) {
            this.repo = repo;
        }

        @Override
        public void remover(ConquistaId id) {
            repo.deleteById(id.getId());
        }

        @Override
        public Optional<Conquista> buscarPorId(ConquistaId id) {
            return repo.findById(id.getId())
                    .map(ConquistaMapper::toDomain);
        }
        @Override
        public void salvar(Conquista conquista) {
            ConquistaJpa entity = ConquistaMapper.toEntity(conquista, false);
            repo.save(entity);
        }

        @Override
        public List<Conquista> listarAtivas() {
            return repo.findByConcluidaFalse()
                    .stream()
                    .map(ConquistaMapper::toDomain)
                    .toList();
        }

        @Override
        public void marcarComoConcluida(Conquista conquista) {
            ConquistaJpa entity = ConquistaMapper.toEntity(conquista, true);
            repo.save(entity);
        }

        @Override
        public List<Conquista> listarConcluidas() {
            return repo.findByConcluidaTrue()
                    .stream()
                    .map(ConquistaMapper::toDomain)
                    .toList();
        }
    }


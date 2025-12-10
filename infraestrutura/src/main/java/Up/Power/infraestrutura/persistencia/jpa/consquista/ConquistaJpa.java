package Up.Power.infraestrutura.persistencia.jpa.consquista;

import Up.Power.conquista.ConquistaId;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Up.Power.Conquista;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.infraestrutura.persistencia.jpa.consquista.JpaConquistaRepository;
import java.util.Optional;
import java.util.Objects;

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
        
        // Critérios para conquistar automaticamente
        @Column(name = "peso_minimo")
        private Float pesoMinimo;
        
        @Column(name = "atributo_minimo")
        private Integer atributoMinimo;
        
        @Column(name = "tipo_atributo", length = 50)
        private String tipoAtributo; // "forca", "resistencia", "agilidade", "nivel"
        
        @Column(name = "repeticoes_minimas")
        private Integer repeticoesMinimas;
        
        @Column(name = "series_minimas")
        private Integer seriesMinimas;

        public ConquistaJpa() {}

        public ConquistaJpa(Integer id, Integer exercicioId, Integer treinoId,
                            String descricao, String nome) {
            this.id = id;
            this.exercicioId = exercicioId;
            this.treinoId = treinoId;
            this.descricao = descricao;
            this.nome = nome;
        }

        public Integer getId() { return id; }
        public Integer getExercicioId() { return exercicioId; }
        public Integer getTreinoId() { return treinoId; }
        public String getDescricao() { return descricao; }
        public String getNome() { return nome; }
        
        // Getters para critérios
        public Float getPesoMinimo() { return pesoMinimo; }
        public Integer getAtributoMinimo() { return atributoMinimo; }
        public String getTipoAtributo() { return tipoAtributo; }
        public Integer getRepeticoesMinimas() { return repeticoesMinimas; }
        public Integer getSeriesMinimas() { return seriesMinimas; }

        public void setId(Integer id) { this.id = id; }
        public void setExercicioId(Integer exercicioId) { this.exercicioId = exercicioId; }
        public void setTreinoId(Integer treinoId) { this.treinoId = treinoId; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public void setNome(String nome) { this.nome = nome; }
        
        // Setters para critérios
        public void setPesoMinimo(Float pesoMinimo) { this.pesoMinimo = pesoMinimo; }
        public void setAtributoMinimo(Integer atributoMinimo) { this.atributoMinimo = atributoMinimo; }
        public void setTipoAtributo(String tipoAtributo) { this.tipoAtributo = tipoAtributo; }
        public void setRepeticoesMinimas(Integer repeticoesMinimas) { this.repeticoesMinimas = repeticoesMinimas; }
        public void setSeriesMinimas(Integer seriesMinimas) { this.seriesMinimas = seriesMinimas; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConquistaJpa that = (ConquistaJpa) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
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
            ConquistaJpa entity = ConquistaMapper.toEntity(conquista);
            repo.save(entity);
        }

        @Override
        public List<Conquista> listarAtivas() {
            // Sem a coluna concluida, retorna todas as conquistas
            // A lógica de "ativa" pode ser gerenciada via relacionamento perfil_conquista
            return repo.findAll()
                    .stream()
                    .map(ConquistaMapper::toDomain)
                    .toList();
        }

        @Override
        public void marcarComoConcluida(Conquista conquista) {
            // Sem a coluna concluida, apenas salva a conquista
            // A marcação de "concluída" deve ser feita via relacionamento perfil_conquista
            salvar(conquista);
        }

        @Override
        public List<Conquista> listarConcluidas() {
            // Sem a coluna concluida, retorna todas as conquistas
            // A lógica de "concluída" pode ser gerenciada via relacionamento perfil_conquista
            return repo.findAll()
                    .stream()
                    .map(ConquistaMapper::toDomain)
                    .toList();
        }
    }


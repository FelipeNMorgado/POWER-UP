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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "plano_refeicoes",
            joinColumns = @JoinColumn(name = "plano_id")
    )
    @Column(name = "refeicao_id")
    // IMPORTANTE: Não usar CascadeType.ALL para ElementCollection
    // O JPA gerencia isso automaticamente através da tabela de junção
    // Usar orphanRemoval=false pois não queremos remover refeições quando removemos do plano
    private List<Integer> refeicoes = new ArrayList<>();

    private int caloriasTotais;
    private int caloriasObjetivo;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    public PlanoNutricionalJpa() {}

    public PlanoNutricionalJpa(Integer id,
                               Up.Power.planoNutricional.Objetivo objetivo,
                               Up.Power.EstadoPlano estado,
                               List<Integer> refeicoes,
                               int caloriasTotais,
                               int caloriasObjetivo,
                               String usuarioEmail) {
        // Se o ID for 0 ou null, definir como null para que o JPA gere automaticamente
        this.id = (id == null || id == 0) ? null : id;
        this.objetivo = objetivo;
        this.estado = estado;
        // IMPORTANTE: Sempre criar uma nova lista para evitar compartilhamento de referência
        // que pode causar problemas com @ElementCollection
        this.refeicoes = refeicoes != null ? new ArrayList<>(refeicoes) : new ArrayList<>();
        this.caloriasTotais = caloriasTotais;
        this.caloriasObjetivo = caloriasObjetivo;
        this.usuarioEmail = usuarioEmail;
        
        System.out.println("[JPA_ENTITY] Construtor chamado. ID=" + this.id + 
                ", Objetivo=" + this.objetivo + 
                ", Refeições=" + this.refeicoes.size() + " itens: " + this.refeicoes);
    }

    public Integer getId() { return id; }
    public Up.Power.planoNutricional.Objetivo getObjetivo() { return objetivo; }
    public Up.Power.EstadoPlano getEstado() { return estado; }
    public List<Integer> getRefeicoes() { return refeicoes; }
    public int getCaloriasTotais() { return caloriasTotais; }
    public int getCaloriasObjetivo() { return caloriasObjetivo; }
    public String getUsuarioEmail() { return usuarioEmail; }

    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }

    public void setId(Integer id) { this.id = id; }
    public void setObjetivo(Up.Power.planoNutricional.Objetivo objetivo) { this.objetivo = objetivo; }
    public void setEstado(Up.Power.EstadoPlano estado) { this.estado = estado; }
    public void setRefeicoes(List<Integer> refeicoes) { this.refeicoes = refeicoes; }
    public void setCaloriasTotais(int caloriasTotais) { this.caloriasTotais = caloriasTotais; }
    public void setCaloriasObjetivo(int caloriasObjetivo) { this.caloriasObjetivo = caloriasObjetivo; }
}

@Repository
interface JpaPlanoNutricionalRepository extends JpaRepository<PlanoNutricionalJpa, Integer> {
    java.util.List<PlanoNutricionalJpa> findByUsuarioEmail(String usuarioEmail);
}

@Repository
class PlanoNutricionalRepositoryImpl implements PlanoNutricionalRepository {

    private final JpaPlanoNutricionalRepository jpaRepository;
    private final PlanoNutricionalMapper mapper;
    private final jakarta.persistence.EntityManager entityManager;

    public PlanoNutricionalRepositoryImpl(
            JpaPlanoNutricionalRepository jpaRepository,
            PlanoNutricionalMapper mapper,
            jakarta.persistence.EntityManager entityManager
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.entityManager = entityManager;
        System.out.println("PlanoNutricionalRepositoryImpl inicializado");
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void salvar(PlanoNutricional plano) {
        System.out.println("[REPOSITORY] ========================================");
        System.out.println("[REPOSITORY] Iniciando salvamento de plano nutricional");
        try {
            System.out.println("[REPOSITORY] Dados do plano recebido:");
            System.out.println("[REPOSITORY]   - objetivo: " + (plano.getObjetivo() != null ? plano.getObjetivo().name() : "null"));
            System.out.println("[REPOSITORY]   - email: " + (plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : "null"));
            System.out.println("[REPOSITORY]   - id atual: " + (plano.getId() != null ? plano.getId().getId() : "null"));
            System.out.println("[REPOSITORY]   - caloriasTotais: " + plano.getCaloriasTotais());
            System.out.println("[REPOSITORY]   - caloriasObjetivo: " + plano.getCaloriasObjetivo());
            System.out.println("[REPOSITORY]   - refeicoes: " + (plano.getRefeicoes() != null ? plano.getRefeicoes().size() + " itens" : "null"));
            
            System.out.println("[REPOSITORY] Convertendo para entidade JPA...");
            PlanoNutricionalJpa entity = mapper.toEntity(plano);
            System.out.println("[REPOSITORY] Entity criada:");
            System.out.println("[REPOSITORY]   - id: " + entity.getId() + " (null = INSERT, não-null = UPDATE)");
            System.out.println("[REPOSITORY]   - objetivo: " + entity.getObjetivo());
            System.out.println("[REPOSITORY]   - estado: " + entity.getEstado());
            System.out.println("[REPOSITORY]   - email: " + entity.getUsuarioEmail());
            System.out.println("[REPOSITORY]   - caloriasTotais: " + entity.getCaloriasTotais());
            System.out.println("[REPOSITORY]   - caloriasObjetivo: " + entity.getCaloriasObjetivo());
            System.out.println("[REPOSITORY]   - refeicoes: " + (entity.getRefeicoes() != null ? entity.getRefeicoes().size() + " itens" : "null"));
            
            System.out.println("[REPOSITORY] Chamando jpaRepository.save()...");
            // Se a entidade tem ID null, será um INSERT (persist)
            // Se a entidade tem ID não-null, será um UPDATE (merge)
            
            // Para UPDATE com @ElementCollection, precisamos garantir que as associações antigas sejam removidas
            // antes de adicionar as novas, pois o JPA pode não fazer isso corretamente
            if (entity.getId() != null) {
                System.out.println("[REPOSITORY] É um UPDATE. Plano ID=" + entity.getId());
                
                // IMPORTANTE: Primeiro, verificar o estado atual no banco ANTES de fazer qualquer mudança
                PlanoNutricionalJpa planoNoBanco = jpaRepository.findById(entity.getId()).orElse(null);
                if (planoNoBanco != null) {
                    System.out.println("[REPOSITORY] Estado atual no banco:");
                    System.out.println("[REPOSITORY]   - Objetivo: " + planoNoBanco.getObjetivo());
                    System.out.println("[REPOSITORY]   - Refeições no banco: " + 
                            (planoNoBanco.getRefeicoes() != null ? planoNoBanco.getRefeicoes().size() + " itens: " + planoNoBanco.getRefeicoes() : "null"));
                }
                
                System.out.println("[REPOSITORY] Estado que será salvo:");
                System.out.println("[REPOSITORY]   - Objetivo: " + entity.getObjetivo());
                System.out.println("[REPOSITORY]   - Refeições a salvar: " + 
                        (entity.getRefeicoes() != null ? entity.getRefeicoes().size() + " itens: " + entity.getRefeicoes() : "null"));
                
                // IMPORTANTE: Sempre remover TODAS as associações antigas ANTES de inserir as novas
                // Isso garante que não haja associações órfãs ou duplicadas
                System.out.println("[REPOSITORY] Removendo TODAS as associações antigas via SQL para plano_id=" + entity.getId());
                int deleted = entityManager.createNativeQuery(
                    "DELETE FROM plano_refeicoes WHERE plano_id = :planoId"
                ).setParameter("planoId", entity.getId()).executeUpdate();
                entityManager.flush(); // Forçar flush para garantir que as remoções sejam aplicadas
                System.out.println("[REPOSITORY] " + deleted + " associações antigas removidas via SQL.");
                
                // IMPORTANTE: Garantir que a lista de refeições da entidade seja uma nova instância
                // para evitar compartilhamento de referência com outras entidades
                List<Integer> refeicoesParaSalvar = new ArrayList<>();
                if (entity.getRefeicoes() != null && !entity.getRefeicoes().isEmpty()) {
                    refeicoesParaSalvar.addAll(entity.getRefeicoes());
                }
                entity.setRefeicoes(refeicoesParaSalvar);
                
                System.out.println("[REPOSITORY] Nova lista de refeições (cópia isolada): " + 
                        refeicoesParaSalvar.size() + " itens: " + refeicoesParaSalvar);
            } else {
                // Para INSERT (novo plano), SEMPRE garantir que a lista esteja vazia
                // Um novo plano nunca deve ter refeições
                System.out.println("[REPOSITORY] É um INSERT (novo plano). Garantindo que a lista está vazia...");
                System.out.println("[REPOSITORY] Refeições ANTES da limpeza: " + 
                        (entity.getRefeicoes() != null ? entity.getRefeicoes().size() + " itens: " + entity.getRefeicoes() : "null"));
                
                // IMPORTANTE: Sempre limpar a lista para novos planos
                entity.setRefeicoes(new ArrayList<>());
                
                System.out.println("[REPOSITORY] Refeições DEPOIS da limpeza: " + 
                        (entity.getRefeicoes() != null ? entity.getRefeicoes().size() + " itens: " + entity.getRefeicoes() : "null"));
                
                // Verificação final
                if (entity.getRefeicoes() != null && !entity.getRefeicoes().isEmpty()) {
                    System.err.println("[REPOSITORY] ERRO CRÍTICO: Lista ainda tem refeições após limpeza! Forçando limpeza...");
                    entity.getRefeicoes().clear();
                }
            }
            
            // IMPORTANTE: Antes de salvar, verificar se há outros planos do mesmo usuário
            // para garantir que não estamos afetando outros planos incorretamente
            if (entity.getId() != null) {
                System.out.println("[REPOSITORY] Verificando outros planos do usuário ANTES de salvar:");
                List<PlanoNutricionalJpa> planosAntes = jpaRepository.findByUsuarioEmail(entity.getUsuarioEmail());
                for (PlanoNutricionalJpa p : planosAntes) {
                    if (!p.getId().equals(entity.getId())) {
                        System.out.println("[REPOSITORY]   - Outro plano ID=" + p.getId() + 
                                ", Objetivo=" + p.getObjetivo() + 
                                ", Refeições=" + (p.getRefeicoes() != null ? p.getRefeicoes().size() + " itens: " + p.getRefeicoes() : "null"));
                    }
                }
            }
            
            PlanoNutricionalJpa salvo = jpaRepository.save(entity);
            System.out.println("[REPOSITORY] jpaRepository.save() concluído com sucesso!");
            System.out.println("[REPOSITORY] ID gerado pelo banco: " + salvo.getId());
            
            // IMPORTANTE: Limpar o cache ANTES de verificar para garantir dados frescos
            entityManager.flush();
            entityManager.clear();
            
            // Recarregar a entidade do banco para verificar se as refeições foram salvas corretamente
            if (salvo.getId() != null) {
                PlanoNutricionalJpa verificado = jpaRepository.findById(salvo.getId()).orElse(null);
                if (verificado != null) {
                    System.out.println("[REPOSITORY] Verificação pós-salvamento do plano salvo:");
                    System.out.println("[REPOSITORY]   - Plano ID=" + verificado.getId() + 
                            ", Objetivo=" + verificado.getObjetivo() + 
                            ", Refeições=" + (verificado.getRefeicoes() != null ? verificado.getRefeicoes().size() + " itens: " + verificado.getRefeicoes() : "null"));
                }
                
                // IMPORTANTE: Verificar TODOS os planos do usuário para garantir que nenhum outro foi afetado
                List<PlanoNutricionalJpa> todosPlanos = jpaRepository.findByUsuarioEmail(entity.getUsuarioEmail());
                System.out.println("[REPOSITORY] Verificando TODOS os planos do usuário após salvar:");
                for (PlanoNutricionalJpa p : todosPlanos) {
                    System.out.println("[REPOSITORY]   - Plano ID=" + p.getId() + 
                            ", Objetivo=" + p.getObjetivo() + 
                            ", Refeições=" + (p.getRefeicoes() != null ? p.getRefeicoes().size() + " itens: " + p.getRefeicoes() : "null"));
                    
                    // Verificar se algum outro plano foi afetado incorretamente
                    if (!p.getId().equals(salvo.getId()) && p.getRefeicoes() != null && !p.getRefeicoes().isEmpty()) {
                        // Verificar se as refeições deste plano são as mesmas que foram salvas no plano atual
                        if (entity.getRefeicoes() != null && !entity.getRefeicoes().isEmpty()) {
                            boolean temRefeicoesIguais = p.getRefeicoes().containsAll(entity.getRefeicoes()) && 
                                                         entity.getRefeicoes().containsAll(p.getRefeicoes());
                            if (temRefeicoesIguais) {
                                System.err.println("[REPOSITORY] ERRO CRÍTICO: Plano ID=" + p.getId() + 
                                        " tem as mesmas refeições que o plano ID=" + salvo.getId() + 
                                        " que acabou de ser salvo! Isso não deveria acontecer!");
                            }
                        }
                    }
                }
            }
            
            // Atualizar o ID do domínio após salvar (caso tenha sido gerado pelo banco)
            if ((plano.getId() == null || plano.getId().getId() == 0) && salvo.getId() != null) {
                System.out.println("[REPOSITORY] Atualizando ID do objeto de domínio...");
                plano.setId(new PlanoNId(salvo.getId()));
                System.out.println("[REPOSITORY] ID atualizado para: " + plano.getId().getId());
            } else {
                System.out.println("[REPOSITORY] ID não precisa ser atualizado (já existe ou não foi gerado)");
            }
            System.out.println("[REPOSITORY] ========================================");
        } catch (Exception e) {
            System.err.println("[REPOSITORY] ========================================");
            System.err.println("[REPOSITORY] ERRO ao salvar plano nutricional:");
            System.err.println("[REPOSITORY] Tipo: " + e.getClass().getName());
            System.err.println("[REPOSITORY] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[REPOSITORY] Causa: " + (e.getCause() != null ? e.getCause().getClass().getName() + " - " + e.getCause().getMessage() : "null"));
            System.err.println("[REPOSITORY] Stack trace:");
            e.printStackTrace();
            System.err.println("[REPOSITORY] ========================================");
            throw e;
        }
    }

    @Override
    public PlanoNutricional obter(PlanoNId id) {
        return jpaRepository.findById(id.getId())
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public java.util.List<PlanoNutricional> listarPorUsuario(String usuarioEmail) {
        System.out.println("[REPOSITORY] ========================================");
        System.out.println("[REPOSITORY] Listando planos para usuário: " + usuarioEmail);
        List<PlanoNutricionalJpa> jpaEntities = jpaRepository.findByUsuarioEmail(usuarioEmail);
        System.out.println("[REPOSITORY] Total de planos encontrados: " + jpaEntities.size());
        
        // IMPORTANTE: Garantir que cada entidade JPA tenha sua própria lista de refeições
        // O JPA pode compartilhar referências de lista entre entidades se não criarmos cópias
        // Além disso, precisamos desconectar as entidades do contexto de persistência
        // para evitar que mudanças em uma entidade afetem outras
        List<PlanoNutricional> planos = new ArrayList<>();
        for (PlanoNutricionalJpa jpa : jpaEntities) {
            System.out.println("[REPOSITORY] Processando plano ID=" + jpa.getId() + 
                    ", Objetivo=" + jpa.getObjetivo() + 
                    ", Refeições ANTES da cópia=" + (jpa.getRefeicoes() != null ? jpa.getRefeicoes().size() + " itens: " + jpa.getRefeicoes() : "null"));
            
            // IMPORTANTE: Criar uma cópia profunda da lista de refeições
            // Isso garante que cada entidade tenha sua própria lista independente
            List<Integer> refeicoesCopy = new ArrayList<>();
            if (jpa.getRefeicoes() != null && !jpa.getRefeicoes().isEmpty()) {
                refeicoesCopy.addAll(jpa.getRefeicoes());
            }
            
            // Criar uma nova entidade JPA com a lista copiada para garantir isolamento completo
            PlanoNutricionalJpa jpaIsolado = new PlanoNutricionalJpa(
                jpa.getId(),
                jpa.getObjetivo(),
                jpa.getEstado(),
                refeicoesCopy, // Usar a cópia
                jpa.getCaloriasTotais(),
                jpa.getCaloriasObjetivo(),
                jpa.getUsuarioEmail()
            );
            
            System.out.println("[REPOSITORY] Plano isolado criado. Refeições DEPOIS da cópia=" + 
                    (jpaIsolado.getRefeicoes() != null ? jpaIsolado.getRefeicoes().size() + " itens: " + jpaIsolado.getRefeicoes() : "null"));
            
            PlanoNutricional dominio = mapper.toDomain(jpaIsolado);
            System.out.println("[REPOSITORY] Domínio convertido. Refeições no domínio=" + 
                    (dominio.getRefeicoes() != null ? dominio.getRefeicoes().size() + " itens" : "null"));
            planos.add(dominio);
        }
        
        System.out.println("[REPOSITORY] Total de planos convertidos: " + planos.size());
        System.out.println("[REPOSITORY] ========================================");
        return planos;
    }

    @Override
    public void excluir(PlanoNId id) {
        System.out.println("[REPOSITORY] Excluindo plano nutricional ID: " + id.getId());
        jpaRepository.deleteById(id.getId());
        System.out.println("[REPOSITORY] Plano nutricional excluído com sucesso");
    }
}
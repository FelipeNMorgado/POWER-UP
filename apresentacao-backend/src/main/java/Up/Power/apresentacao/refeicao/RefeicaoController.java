package Up.Power.apresentacao.refeicao;

import Up.Power.Refeicao;
import Up.Power.Alimento;
import Up.Power.alimento.AlimentoId;
import Up.Power.alimento.AlimentoRepository;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.refeicao.RefeicaoRepository;
import Up.Power.refeicao.TipoRefeicao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/refeicoes")
public class RefeicaoController {

    private final RefeicaoRepository refeicaoRepository;
    private final AlimentoRepository alimentoRepository;

    public RefeicaoController(RefeicaoRepository refeicaoRepository, AlimentoRepository alimentoRepository) {
        this.refeicaoRepository = refeicaoRepository;
        this.alimentoRepository = alimentoRepository;
    }

    @PostMapping
    public ResponseEntity<Refeicao> criar(@RequestBody CriarRefeicaoRequest request) {
        System.out.println("========================================");
        System.out.println("[REFEICAO_CONTROLLER] Recebendo requisição para criar refeição");
        System.out.println("[REFEICAO_CONTROLLER] Request: tipo=" + request.tipo() + 
                ", alimentosIds=" + (request.alimentosIds() != null ? request.alimentosIds().size() + " itens" : "null") +
                ", caloriasTotais=" + request.caloriasTotais() +
                ", inicio=" + request.inicio() +
                ", fim=" + request.fim());
        try {
            // Validar campo obrigatório
            if (request.tipo() == null) {
                System.err.println("[REFEICAO_CONTROLLER] ERRO: Campo 'tipo' é obrigatório");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }
            System.out.println("[REFEICAO_CONTROLLER] Criando objeto Refeicao...");
            Refeicao refeicao = new Refeicao(new RefeicaoId(0), request.tipo());
            System.out.println("[REFEICAO_CONTROLLER] Refeicao criada. ID inicial: " + 
                    (refeicao.getId() != null ? refeicao.getId().getId() : "null"));
            
            // Calcular calorias totais baseado nos alimentos selecionados
            int caloriasTotais = 0;
            if (request.alimentosIds() != null && !request.alimentosIds().isEmpty()) {
                System.out.println("[REFEICAO_CONTROLLER] Adicionando " + request.alimentosIds().size() + " alimentos...");
                
                // Buscar todos os alimentos uma única vez e criar um mapa para acesso rápido
                List<Alimento> todosAlimentos = alimentoRepository.listarTodos();
                java.util.Map<Integer, Alimento> alimentosMap = todosAlimentos.stream()
                        .filter(a -> a.getAlimento() != null)
                        .collect(java.util.stream.Collectors.toMap(
                                a -> a.getAlimento().getId(),
                                a -> a
                        ));
                
                for (Integer alimentoId : request.alimentosIds()) {
                    System.out.println("[REFEICAO_CONTROLLER] Processando alimento ID: " + alimentoId);
                    Alimento alimento = alimentosMap.get(alimentoId);
                    
                    if (alimento != null) {
                        System.out.println("[REFEICAO_CONTROLLER] Alimento encontrado: " + alimento.getNome() + " - " + alimento.getCalorias() + " cal");
                        caloriasTotais += alimento.getCalorias();
                        refeicao.adicionarAlimento(new AlimentoId(alimentoId));
                    } else {
                        System.err.println("[REFEICAO_CONTROLLER] AVISO: Alimento com ID " + alimentoId + " não encontrado");
                    }
                }
                System.out.println("[REFEICAO_CONTROLLER] Total de alimentos adicionados: " + 
                        (refeicao.getAlimentos() != null ? refeicao.getAlimentos().size() : 0));
                System.out.println("[REFEICAO_CONTROLLER] Calorias totais calculadas: " + caloriasTotais);
            } else {
                System.out.println("[REFEICAO_CONTROLLER] Nenhum alimento para adicionar (lista vazia ou null)");
            }
            
            // Definir calorias totais calculadas automaticamente (ignorar o valor enviado pelo frontend se houver)
            refeicao.setCaloriasTotais(caloriasTotais);
            
            System.out.println("[REFEICAO_CONTROLLER] Definindo datas: inicio=" + request.inicio() + ", fim=" + request.fim());
            refeicao.setInicio(request.inicio());
            refeicao.setFim(request.fim());

            System.out.println("[REFEICAO_CONTROLLER] Chamando repository.salvar...");
            Refeicao salvo = refeicaoRepository.salvar(refeicao);
            System.out.println("[REFEICAO_CONTROLLER] Refeicao salva com sucesso. ID: " + 
                    (salvo.getId() != null ? salvo.getId().getId() : "null"));
            System.out.println("========================================");
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("[REFEICAO_CONTROLLER] ERRO ao criar refeição:");
            System.err.println("[REFEICAO_CONTROLLER] Tipo: " + e.getClass().getName());
            System.err.println("[REFEICAO_CONTROLLER] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[REFEICAO_CONTROLLER] Causa: " + (e.getCause() != null ? e.getCause().getClass().getName() + " - " + e.getCause().getMessage() : "null"));
            System.err.println("[REFEICAO_CONTROLLER] Stack trace completo:");
            e.printStackTrace();
            System.err.println("========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Refeicao> obter(@PathVariable("id") Integer id) {
        try {
            Refeicao refeicao = refeicaoRepository.obter(new RefeicaoId(id));
            if (refeicao == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(refeicao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Refeicao>> listar() {
        try {
            List<Refeicao> refeicoes = refeicaoRepository.listar(null);
            return ResponseEntity.ok(refeicoes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Integer id) {
        try {
            refeicaoRepository.excluir(new RefeicaoId(id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DTO
    public record CriarRefeicaoRequest(
            TipoRefeicao tipo,
            List<Integer> alimentosIds,
            Integer caloriasTotais, // Campo opcional, será ignorado - calorias serão calculadas automaticamente
            Date inicio,
            Date fim
    ) {}
}


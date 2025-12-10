package Up.Power.apresentacao.alimento;

import Up.Power.Alimento;
import Up.Power.alimento.AlimentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoRepository alimentoRepository;

    public AlimentoController(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @GetMapping
    public ResponseEntity<List<AlimentoResponse>> listarTodos() {
        System.out.println("========================================");
        System.out.println("[ALIMENTO_CONTROLLER] Recebendo requisição para listar todos os alimentos");
        try {
            List<Alimento> alimentos = alimentoRepository.listarTodos();
            System.out.println("[ALIMENTO_CONTROLLER] Total de alimentos encontrados: " + alimentos.size());
            
            List<AlimentoResponse> responses = alimentos.stream()
                    .map(a -> {
                        System.out.println("[ALIMENTO_CONTROLLER] Processando alimento: id=" + 
                                (a.getAlimento() != null ? a.getAlimento().getId() : "null") + 
                                ", nome=" + a.getNome() + 
                                ", categoria=" + (a.getCategoria() != null ? a.getCategoria().name() : "null") +
                                ", calorias=" + a.getCalorias());
                        return new AlimentoResponse(
                                a.getAlimento() != null ? a.getAlimento().getId() : 0,
                                a.getNome(),
                                a.getCategoria() != null ? a.getCategoria().name() : "Gordura",
                                a.getCalorias(),
                                a.getGramas()
                        );
                    })
                    .toList();
            
            System.out.println("[ALIMENTO_CONTROLLER] Retornando " + responses.size() + " alimentos");
            System.out.println("========================================");
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("[ALIMENTO_CONTROLLER] ERRO ao listar alimentos:");
            System.err.println("[ALIMENTO_CONTROLLER] Tipo: " + e.getClass().getName());
            System.err.println("[ALIMENTO_CONTROLLER] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[ALIMENTO_CONTROLLER] Stack trace:");
            e.printStackTrace();
            System.err.println("========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DTO para resposta
    public record AlimentoResponse(
            int id,
            String nome,
            String categoria,
            int calorias,
            float gramas
    ) {}
}


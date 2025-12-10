package Up.Power.apresentacao.planoNutricional;

import Up.Power.aplicacao.planoNutricional.service.PlanoNutricionalApplicationService;
import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planos-nutricionais")
public class PlanoNutricionalController {

    private final PlanoNutricionalApplicationService planoNutricionalService;

    public PlanoNutricionalController(PlanoNutricionalApplicationService planoNutricionalService) {
        this.planoNutricionalService = planoNutricionalService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarPlanoNutricionalCommand command) {
        System.out.println("========================================");
        System.out.println("[CONTROLLER] Recebendo requisição para criar plano nutricional");
        System.out.println("[CONTROLLER] Command recebido: objetivo=" + command.objetivo() + 
                ", usuarioEmail=" + command.usuarioEmail() + 
                ", caloriasObjetivo=" + command.caloriasObjetivo() + 
                ", refeicoesIds=" + (command.refeicoesIds() != null ? command.refeicoesIds().size() : "null"));
        try {
            // Validar campos obrigatórios
            System.out.println("[CONTROLLER] Validando campos obrigatórios...");
            if (command.objetivo() == null || command.usuarioEmail() == null || command.usuarioEmail().isEmpty()) {
                System.out.println("[CONTROLLER] ERRO: Campos obrigatórios inválidos");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"erro\": \"Campos obrigatórios: objetivo e usuarioEmail\"}");
            }
            System.out.println("[CONTROLLER] Validação OK. Chamando service...");
            
            PlanoNutricional plano = planoNutricionalService.criar(command);
            System.out.println("[CONTROLLER] Service retornou plano. ID=" + 
                    (plano != null && plano.getId() != null ? plano.getId().getId() : "null"));
            
            // Criar DTO para resposta
            System.out.println("[CONTROLLER] Criando DTO de resposta...");
            System.out.println("[CONTROLLER] plano.getId()=" + (plano != null && plano.getId() != null ? plano.getId().getId() : "null"));
            System.out.println("[CONTROLLER] plano.getObjetivo()=" + (plano != null && plano.getObjetivo() != null ? plano.getObjetivo().name() : "null"));
            System.out.println("[CONTROLLER] plano.getRefeicoes()=" + (plano != null && plano.getRefeicoes() != null ? plano.getRefeicoes().size() : "null"));
            System.out.println("[CONTROLLER] plano.getCaloriasTotais()=" + (plano != null ? plano.getCaloriasTotais() : "null"));
            System.out.println("[CONTROLLER] plano.getCaloriasObjetivo()=" + (plano != null ? plano.getCaloriasObjetivo() : "null"));
            System.out.println("[CONTROLLER] plano.getUsuarioEmail()=" + (plano != null && plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : "null"));
            
            PlanoNutricionalResponse response = new PlanoNutricionalResponse(
                    plano.getId().getId(),
                    plano.getObjetivo().name(),
                    plano.getRefeicoes() != null 
                        ? plano.getRefeicoes().stream().map(r -> r.getId()).toList()
                        : java.util.Collections.emptyList(),
                    plano.getCaloriasTotais(),
                    plano.getCaloriasObjetivo(),
                    plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : null
            );
            
            System.out.println("[CONTROLLER] DTO criado com sucesso. Retornando resposta OK.");
            System.out.println("========================================");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.err.println("[CONTROLLER] IllegalArgumentException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("[CONTROLLER] ERRO CRÍTICO ao criar plano nutricional:");
            System.err.println("[CONTROLLER] Tipo da exceção: " + e.getClass().getName());
            System.err.println("[CONTROLLER] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[CONTROLLER] Causa: " + (e.getCause() != null ? e.getCause().getClass().getName() + " - " + e.getCause().getMessage() : "null"));
            System.err.println("[CONTROLLER] Stack trace completo:");
            e.printStackTrace();
            System.err.println("========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"erro\": \"" + (e.getMessage() != null ? e.getMessage() : "Erro desconhecido") + "\"}");
        }
    }
    
    // DTO para resposta
    public record PlanoNutricionalResponse(
            int id,
            String objetivo,
            List<Integer> refeicoes,
            int caloriasTotais,
            int caloriasObjetivo,
            String usuarioEmail
    ) {}

    @PutMapping
    public ResponseEntity<PlanoNutricional> modificar(@RequestBody ModificarPlanoNutricionalCommand command) {
        try {
            PlanoNutricional plano = planoNutricionalService.modificar(command);
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<PlanoNutricionalResponse>> listarPorUsuario(@PathVariable("email") String email) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            System.out.println("========================================");
            System.out.println("[CONTROLLER] Listando planos para usuário: " + emailDecodificado);
            List<PlanoNutricional> planos = planoNutricionalService.listarPorUsuario(emailDecodificado);
            System.out.println("[CONTROLLER] Total de planos encontrados: " + planos.size());
            
            // Converter para DTOs
            List<PlanoNutricionalResponse> responses = planos.stream()
                    .map(plano -> {
                        List<Integer> refeicoesIds = plano.getRefeicoes() != null 
                            ? plano.getRefeicoes().stream().map(r -> r.getId()).toList()
                            : java.util.Collections.emptyList();
                        
                        System.out.println("[CONTROLLER] Plano ID=" + plano.getId().getId() + 
                                ", Objetivo=" + plano.getObjetivo().name() + 
                                ", Refeições=" + refeicoesIds.size() + " IDs: " + refeicoesIds);
                        
                        return new PlanoNutricionalResponse(
                                plano.getId().getId(),
                                plano.getObjetivo().name(),
                                refeicoesIds,
                                plano.getCaloriasTotais(),
                                plano.getCaloriasObjetivo(),
                                plano.getUsuarioEmail() != null ? plano.getUsuarioEmail().getCaracteres() : null
                        );
                    })
                    .toList();
            
            System.out.println("[CONTROLLER] Retornando " + responses.size() + " planos");
            System.out.println("========================================");
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            System.err.println("[CONTROLLER] ERRO ao listar planos:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        try {
            System.out.println("[CONTROLLER] Recebendo requisição para excluir plano nutricional ID: " + id);
            planoNutricionalService.excluir(id.intValue());
            System.out.println("[CONTROLLER] Plano nutricional excluído com sucesso");
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            System.err.println("[CONTROLLER] IllegalArgumentException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.err.println("[CONTROLLER] ERRO ao excluir plano nutricional:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"erro\": \"" + (e.getMessage() != null ? e.getMessage() : "Erro desconhecido") + "\"}");
        }
    }
}


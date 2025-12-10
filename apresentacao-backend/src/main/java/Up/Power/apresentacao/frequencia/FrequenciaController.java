package Up.Power.apresentacao.frequencia;

import Up.Power.aplicacao.frequencia.FrequenciaServicoAplicacao;
import Up.Power.aplicacao.frequencia.FrequenciaResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/frequencias")
public class FrequenciaController {

    private final FrequenciaServicoAplicacao frequenciaServicoAplicacao;

    public FrequenciaController(FrequenciaServicoAplicacao frequenciaServicoAplicacao) {
        this.frequenciaServicoAplicacao = frequenciaServicoAplicacao;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FrequenciaResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            FrequenciaResumo frequencia = frequenciaServicoAplicacao.obterPorId(id);
            if (frequencia == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(frequencia);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<FrequenciaResumo>> listarPorPerfil(@PathVariable("perfilId") Integer perfilId) {
        try {
            List<FrequenciaResumo> frequencias = frequenciaServicoAplicacao.listarPorPerfil(perfilId);
            return ResponseEntity.ok(frequencias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarPresenca(@RequestBody RegistrarPresencaRequest request) {
        try {
            frequenciaServicoAplicacao.registrarPresenca(
                request.perfilId(),
                request.treinoId(),
                request.planoTreinoId()
            );
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Presença registrada com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/registrar-automatico")
    public ResponseEntity<Map<String, String>> registrarPresencaAutomatica(@RequestBody RegistrarPresencaAutomaticaRequest request) {
        try {
            frequenciaServicoAplicacao.registrarPresencaAutomatica(
                request.perfilId(),
                request.usuarioEmail()
            );
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Presença registrada com sucesso");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "Erro ao registrar presença. Tente novamente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/registrar-com-foto")
    public ResponseEntity<Map<String, String>> registrarPresencaComFoto(@RequestBody RegistrarPresencaComFotoRequest request) {
        try {
            frequenciaServicoAplicacao.registrarPresencaComFoto(
                request.perfilId(),
                request.treinoId(),
                request.planoTreinoId(),
                request.fotoBase64()
            );
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Presença registrada com foto com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/registrar-com-foto-automatico")
    public ResponseEntity<Map<String, String>> registrarPresencaComFotoAutomatica(@RequestBody RegistrarPresencaComFotoAutomaticaRequest request) {
        try {
            frequenciaServicoAplicacao.registrarPresencaComFotoAutomatica(
                request.perfilId(),
                request.usuarioEmail(),
                request.fotoBase64()
            );
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Presença registrada com foto com sucesso");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "Erro ao registrar presença com foto. Tente novamente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/sequencia/{perfilId}/{planoTreinoId}")
    public ResponseEntity<Map<String, Integer>> calcularSequenciaDias(
            @PathVariable("perfilId") Integer perfilId,
            @PathVariable("planoTreinoId") Integer planoTreinoId) {
        try {
            int sequencia = frequenciaServicoAplicacao.calcularSequenciaDias(perfilId, planoTreinoId);
            Map<String, Integer> response = new HashMap<>();
            response.put("sequencia", sequencia);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/semanal/{perfilId}/{planoTreinoId}")
    public ResponseEntity<Map<String, Integer>> calcularFrequenciaSemanal(
            @PathVariable("perfilId") Integer perfilId,
            @PathVariable("planoTreinoId") Integer planoTreinoId) {
        try {
            int frequencia = frequenciaServicoAplicacao.calcularFrequenciaSemanal(perfilId, planoTreinoId);
            Map<String, Integer> response = new HashMap<>();
            response.put("frequencia", frequencia);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sequencia-total/{perfilId}")
    public ResponseEntity<Map<String, Integer>> calcularSequenciaDiasTotal(
            @PathVariable("perfilId") Integer perfilId) {
        try {
            int sequencia = frequenciaServicoAplicacao.calcularSequenciaDiasTotal(perfilId);
            Map<String, Integer> response = new HashMap<>();
            response.put("sequencia", sequencia);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public record RegistrarPresencaRequest(
            Integer perfilId,
            Integer treinoId,
            Integer planoTreinoId
    ) {}

    public record RegistrarPresencaAutomaticaRequest(
            Integer perfilId,
            String usuarioEmail
    ) {}

    public record RegistrarPresencaComFotoRequest(
            Integer perfilId,
            Integer treinoId,
            Integer planoTreinoId,
            String fotoBase64
    ) {}

    public record RegistrarPresencaComFotoAutomaticaRequest(
            Integer perfilId,
            String usuarioEmail,
            String fotoBase64
    ) {}
}


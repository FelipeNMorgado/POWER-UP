package Up.Power.apresentacao.frequencia;

import Up.Power.aplicacao.frequencia.FrequenciaServicoAplicacao;
import Up.Power.aplicacao.frequencia.FrequenciaResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}


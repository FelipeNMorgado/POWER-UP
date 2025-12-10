package Up.Power.apresentacao.exercicio;

import Up.Power.exercicio.ExercicioRepository;
import Up.Power.exercicio.ExercicioId;
import Up.Power.Exercicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final ExercicioRepository exercicioRepository;

    public ExercicioController(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    @GetMapping
    public ResponseEntity<List<ExercicioResponse>> listarTodos() {
        try {
            List<ExercicioResponse> exercicios = exercicioRepository.listarTodos().stream()
                    .map(exercicio -> new ExercicioResponse(
                            exercicio.getId(0).getId(),
                            exercicio.getNome()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(exercicios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponse> obterPorId(@PathVariable("id") Integer id) {
        try {
            return exercicioRepository.obter(new ExercicioId(id))
                    .map(exercicio -> new ExercicioResponse(exercicio.getId(0).getId(), exercicio.getNome()))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public record ExercicioResponse(Integer id, String nome) {}
}


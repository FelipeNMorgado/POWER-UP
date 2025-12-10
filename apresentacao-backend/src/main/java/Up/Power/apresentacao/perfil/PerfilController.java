package Up.Power.apresentacao.perfil;

import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.Perfil;
import Up.Power.aplicacao.perfil.PerfilResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilRepository perfilRepository;

    public PerfilController(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            Optional<Perfil> perfilOpt = perfilRepository.findById(new PerfilId(id));
            if (perfilOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Perfil perfil = perfilOpt.get();
            PerfilResumo resumo = toResumo(perfil);
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<PerfilResumo> obterPorEmail(@PathVariable("email") String email) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            Optional<Perfil> perfilOpt = perfilRepository.findByUsuarioEmail(emailDecodificado);
            if (perfilOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Perfil perfil = perfilOpt.get();
            PerfilResumo resumo = toResumo(perfil);
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResumo> atualizarPerfil(
            @PathVariable("id") Integer id,
            @RequestBody AtualizarPerfilRequest request) {
        try {
            Optional<Perfil> perfilOpt = perfilRepository.findById(new PerfilId(id));
            if (perfilOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Perfil perfil = perfilOpt.get();

            // Atualizar campos se fornecidos
            if (request.username() != null) {
                perfil.setUsername(request.username());
            }
            if (request.foto() != null) {
                perfil.setFoto(request.foto());
            }
            if (request.estado() != null) {
                perfil.setEstado(request.estado());
            }

            Perfil perfilAtualizado = perfilRepository.save(perfil);
            PerfilResumo resumo = toResumo(perfilAtualizado);
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private PerfilResumo toResumo(Perfil perfil) {
        return new PerfilResumo(
                perfil.getId() != null ? perfil.getId().getId() : null,
                perfil.getUsuarioEmail() != null ? perfil.getUsuarioEmail().getCaracteres() : null,
                perfil.getUsername(),
                perfil.getFoto(),
                perfil.isEstado(),
                perfil.getCriacao()
        );
    }

    public record AtualizarPerfilRequest(
            String username,
            String foto,
            Boolean estado
    ) {}
}


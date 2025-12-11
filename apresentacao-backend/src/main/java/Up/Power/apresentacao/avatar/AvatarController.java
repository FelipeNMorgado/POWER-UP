package Up.Power.apresentacao.avatar;

import Up.Power.aplicacao.avatar.AvatarServicoAplicacao;
import Up.Power.aplicacao.avatar.AvatarResumo;
import Up.Power.aplicacao.avatar.AdicionarXpCommand;
import Up.Power.aplicacao.avatar.AvatarServicoAplicacao.AtributosCalculadosResumo;
import Up.Power.aplicacao.avatar.EquiparAcessoriosCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    private final AvatarServicoAplicacao avatarServicoAplicacao;

    public AvatarController(AvatarServicoAplicacao avatarServicoAplicacao) {
        this.avatarServicoAplicacao = avatarServicoAplicacao;
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<AvatarResumo> obterPorPerfilId(@PathVariable("perfilId") Integer perfilId) {
        try {
            AvatarResumo avatar = avatarServicoAplicacao.obterPorPerfilId(perfilId);
            if (avatar == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(avatar);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/adicionar-xp")
    public ResponseEntity<Void> adicionarXp(@RequestBody AdicionarXpCommand command) {
        try {
            avatarServicoAplicacao.adicionarXp(command);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{avatarId}/atributos")
    public ResponseEntity<AtributosCalculadosResumo> obterAtributosCalculados(@PathVariable("avatarId") Integer avatarId) {
        try {
            AtributosCalculadosResumo atributos = avatarServicoAplicacao.obterAtributosCalculados(avatarId);
            return ResponseEntity.ok(atributos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{avatarId}/acessorios")
    public ResponseEntity<AvatarResumo> equiparAcessorios(
            @PathVariable("avatarId") Integer avatarId,
            @RequestBody EquiparAcessoriosCommand command
    ) {
        try {
            if (command.avatarId() != null && !command.avatarId().equals(avatarId)) {
                return ResponseEntity.badRequest().build();
            }
            if (command.acessorioIds() == null) {
                return ResponseEntity.badRequest().build();
            }
            var resumo = avatarServicoAplicacao.equiparAcessorios(avatarId, command.acessorioIds());
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/perfil/{perfilId}/acessorios")
    public ResponseEntity<AvatarResumo> equiparAcessoriosPorPerfil(
            @PathVariable("perfilId") Integer perfilId,
            @RequestBody EquiparAcessoriosCommand command
    ) {
        try {
            if (command.acessorioIds() == null) {
                return ResponseEntity.badRequest().build();
            }
            var resumo = avatarServicoAplicacao.equiparAcessoriosPorPerfil(perfilId, command.acessorioIds());
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


package Up.Power.apresentacao.usuario;

import Up.Power.aplicacao.usuario.UsuarioServicoAplicacao;
import Up.Power.aplicacao.usuario.UsuarioResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServicoAplicacao usuarioServicoAplicacao;

    public UsuarioController(UsuarioServicoAplicacao usuarioServicoAplicacao) {
        this.usuarioServicoAplicacao = usuarioServicoAplicacao;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResumo> obterPorEmail(@PathVariable("email") String email) {
        try {
            // Decodificar o email caso tenha sido URL-encoded
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            UsuarioResumo usuario = usuarioServicoAplicacao.obterPorEmail(emailDecodificado);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{email}/amigos")
    public ResponseEntity<List<UsuarioResumo>> listarAmigos(@PathVariable("email") String email) {
        try {
            String emailDecodificado = java.net.URLDecoder.decode(email, "UTF-8");
            List<UsuarioResumo> amigos = usuarioServicoAplicacao.listarAmigos(emailDecodificado);
            return ResponseEntity.ok(amigos);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{emailRemetente}/adicionar-amigo/{codigoAmizade}")
    public ResponseEntity<String> adicionarAmigoPorCodigo(
            @PathVariable("emailRemetente") String emailRemetente,
            @PathVariable("codigoAmizade") int codigoAmizade) {
        try {
            String emailRemetenteDecodificado = java.net.URLDecoder.decode(emailRemetente, "UTF-8");
            String resultado = usuarioServicoAplicacao.adicionarAmigoPorCodigo(emailRemetenteDecodificado, codigoAmizade);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar amigo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{email1}/amizade/{email2}")
    public ResponseEntity<String> removerAmizade(
            @PathVariable("email1") String email1,
            @PathVariable("email2") String email2) {
        try {
            String email1Decodificado = java.net.URLDecoder.decode(email1, "UTF-8");
            String email2Decodificado = java.net.URLDecoder.decode(email2, "UTF-8");
            String resultado = usuarioServicoAplicacao.removerAmizade(email1Decodificado, email2Decodificado);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover amizade: " + e.getMessage());
        }
    }

    @GetMapping("/{email1}/sao-amigos/{email2}")
    public ResponseEntity<Boolean> saoAmigos(
            @PathVariable("email1") String email1,
            @PathVariable("email2") String email2) {
        try {
            String email1Decodificado = java.net.URLDecoder.decode(email1, "UTF-8");
            String email2Decodificado = java.net.URLDecoder.decode(email2, "UTF-8");
            boolean saoAmigos = usuarioServicoAplicacao.saoAmigos(email1Decodificado, email2Decodificado);
            return ResponseEntity.ok(saoAmigos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


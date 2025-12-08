package Up.Power.apresentacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Up.Power.AmizadeId;
import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // -----------------------------
    // Criar usuário
    // -----------------------------
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody CriarUsuarioRequest request) {
        try {
            Usuario novo = usuarioService.criarUsuario(
                    new Email(request.email()),
                    request.nome(),
                    request.senha(),
                    request.dataNascimento()
            );
            return ResponseEntity.ok(novo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Obter por email
    // -----------------------------
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obterPorEmail(@PathVariable("email") String email) {
        try {
            Usuario u = usuarioService.obterUsuario(new Email(email));
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Obter por ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Integer id) {
        try {
            Usuario u = usuarioService.obterUsuarioPorId(id);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Atualizar nome
    // -----------------------------
    @PutMapping("/{email}/nome")
    public ResponseEntity<?> atualizarNome(@PathVariable("email") String email,
                                           @RequestBody Map<String, String> body) {
        try {
            usuarioService.atualizarNome(new Email(email), body.get("nome"));
            return ResponseEntity.ok(Map.of("mensagem", "Nome atualizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Atualizar senha
    // -----------------------------
    @PutMapping("/{email}/senha")
    public ResponseEntity<?> atualizarSenha(@PathVariable("email") String email,
                                            @RequestBody Map<String, String> body) {
        try {
            usuarioService.atualizarSenha(new Email(email), body.get("senha"));
            return ResponseEntity.ok(Map.of("mensagem", "Senha atualizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Validar senha
    // -----------------------------
    @PostMapping("/{email}/validar-senha")
    public ResponseEntity<?> validarSenha(@PathVariable("email") String email,
                                          @RequestBody Map<String, String> body) {
        try {
            boolean valido = usuarioService.validarSenha(new Email(email), body.get("senha"));
            return ResponseEntity.ok(Map.of("valida", valido));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Definir código de amizade
    // -----------------------------
    @PutMapping("/{email}/amizade")
    public ResponseEntity<?> definirAmizade(@PathVariable("email") String email,
                                            @RequestBody Map<String, Integer> body) {
        try {
            usuarioService.definirCodigoAmizade(
                    new Email(email),
                    new AmizadeId(body.get("codigo"))
            );
            return ResponseEntity.ok(Map.of("mensagem", "Código de amizade definido"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Listar todos
    // -----------------------------
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    // -----------------------------
    // Deletar por ID
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPorId(@PathVariable("id") Integer id) {
        try {
            usuarioService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário deletado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // -----------------------------
    // Atualizar objeto inteiro
    // -----------------------------
    @PutMapping
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.atualizar(usuario);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário atualizado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ================================
    // RECORD PARA REQUEST DE CRIAÇÃO
    // ================================
    public record CriarUsuarioRequest(
            String email,
            String nome,
            String senha,
            LocalDate dataNascimento
    ) {}
}

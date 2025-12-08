package Up.Power.apresentacao.auth;

import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioRepository;
import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.Perfil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public RegistroController(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @PostMapping("/registro")
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistroRequest request) {
        try {
            // Verificar se o usuário já existe
            Usuario usuarioExistente = usuarioRepository.obter(new Email(request.email()));
            if (usuarioExistente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new RegistroResponse(false, "Email já cadastrado", null, null, null));
            }

            // Criar novo usuário
            Usuario novoUsuario = new Usuario(
                    new Email(request.email()),
                    request.nome(),
                    request.senha(),
                    request.dataNascimento()
            );

            // Salvar usuário
            usuarioRepository.salvar(novoUsuario);

            // Criar perfil associado
            // ID será gerado pelo banco, então usamos 0 temporariamente
            Perfil novoPerfil = new Perfil(
                    new PerfilId(0), // ID será gerado pelo banco
                    new Email(request.email()),
                    request.username() != null ? request.username() : request.nome()
            );

            // Salvar perfil
            Perfil perfilSalvo = perfilRepository.save(novoPerfil);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegistroResponse(
                            true,
                            "Usuário registrado com sucesso",
                            request.email(),
                            perfilSalvo.getId() != null ? perfilSalvo.getId().getId() : null,
                            perfilSalvo.getUsername()
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegistroResponse(false, "Dados inválidos: " + e.getMessage(), null, null, null));
        } catch (Exception e) {
            // Log da exceção completa para debug
            e.printStackTrace();
            String mensagemErro = e.getMessage();
            if (mensagemErro == null || mensagemErro.isEmpty()) {
                mensagemErro = e.getClass().getSimpleName() + ": " + 
                    (e.getCause() != null ? e.getCause().getMessage() : "Erro desconhecido");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegistroResponse(false, "Erro ao registrar usuário: " + mensagemErro, null, null, null));
        }
    }
}


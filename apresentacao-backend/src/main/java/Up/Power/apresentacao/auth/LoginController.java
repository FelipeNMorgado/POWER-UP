package Up.Power.apresentacao.auth;

import Up.Power.aplicacao.usuario.UsuarioServicoAplicacao;
import Up.Power.perfil.PerfilRepository;
import Up.Power.Perfil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UsuarioServicoAplicacao usuarioServicoAplicacao;
    private final PerfilRepository perfilRepository;

    public LoginController(
            UsuarioServicoAplicacao usuarioServicoAplicacao,
            PerfilRepository perfilRepository) {
        this.usuarioServicoAplicacao = usuarioServicoAplicacao;
        this.perfilRepository = perfilRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            // Buscar usuário por email
            var usuarioResumo = usuarioServicoAplicacao.obterPorEmail(request.email());
            
            if (usuarioResumo == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(false, "Usuário não encontrado", null, null, null));
            }

            // Verificar senha (comparação simples - em produção, usar hash)
            if (!usuarioResumo.senha().equals(request.senha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(false, "Senha incorreta", null, null, null));
            }

            // Buscar perfil do usuário
            Optional<Perfil> perfilOpt = Optional.empty();
            // Tentar usar findByUsuarioEmail se disponível (via cast para PerfilRepositoryImpl)
            if (perfilRepository instanceof Up.Power.infraestrutura.persistencia.jpa.perfil.PerfilRepositoryImpl) {
                var repoImpl = (Up.Power.infraestrutura.persistencia.jpa.perfil.PerfilRepositoryImpl) perfilRepository;
                perfilOpt = repoImpl.findByUsuarioEmail(request.email());
            }

            Integer perfilId = null;
            String username = null;
            if (perfilOpt.isPresent()) {
                Perfil perfil = perfilOpt.get();
                perfilId = perfil.getId() != null ? perfil.getId().getId() : null;
                username = perfil.getUsername();
            }

            return ResponseEntity.ok(new LoginResponse(
                    true,
                    "Login realizado com sucesso",
                    request.email(),
                    perfilId,
                    username
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Erro ao realizar login: " + e.getMessage(), null, null, null));
        }
    }
}


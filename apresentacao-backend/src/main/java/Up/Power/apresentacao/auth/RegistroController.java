package Up.Power.apresentacao.auth;

import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.AmizadeId;
import Up.Power.usuario.UsuarioRepository;
import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.Perfil;
import Up.Power.Avatar;
import Up.Power.avatar.AvatarRepository;
import Up.Power.avatar.AvatarId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final AvatarRepository avatarRepository;

    public RegistroController(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            AvatarRepository avatarRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.avatarRepository = avatarRepository;
    }

    @PostMapping("/registro")
    @Transactional
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistroRequest request) {
        try {
            // Verificar se o usuário já existe
            Usuario usuarioExistente = usuarioRepository.obter(new Email(request.email()));
            if (usuarioExistente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new RegistroResponse(false, "Email já cadastrado", null, null, null, null));
            }

            // Criar novo usuário
            Usuario novoUsuario = new Usuario(
                    new Email(request.email()),
                    request.nome(),
                    request.senha(),
                    request.dataNascimento()
            );

            // Atribuir um código de amizade único na criação
            int proximoCodigoAmizade = usuarioRepository.obterProximoAmizadeId();
            novoUsuario.setCodigoAmizade(new AmizadeId(proximoCodigoAmizade));

            // Salvar usuário
            usuarioRepository.salvar(novoUsuario);

            // Criar perfil associado automaticamente após o cadastro
            // Verificar se já existe um perfil para este email (caso de retry ou erro anterior)
            Email emailUsuario = new Email(request.email());
            Perfil perfilExistente = perfilRepository.findByUsuarioEmail(emailUsuario.getCaracteres())
                    .orElse(null);
            
            Perfil perfilSalvo;
            if (perfilExistente == null) {
                // Criar novo perfil
                // ID será gerado pelo banco, então usamos 0 temporariamente
                Perfil novoPerfil = new Perfil(
                        new PerfilId(0), // ID será gerado pelo banco
                        emailUsuario,
                        request.username() != null && !request.username().trim().isEmpty() 
                                ? request.username() 
                                : request.nome()
                );

                // Salvar perfil
                perfilSalvo = perfilRepository.save(novoPerfil);
            } else {
                // Se já existe perfil, usar o existente
                perfilSalvo = perfilExistente;
            }

            // Criar avatar associado automaticamente após o cadastro
            // Verificar se já existe um avatar para este perfil (caso de retry ou erro anterior)
            PerfilId perfilIdSalvo = perfilSalvo.getId();
            if (perfilIdSalvo != null) {
                Avatar avatarExistente = avatarRepository.findByPerfilId(perfilIdSalvo)
                        .orElse(null);
                
                if (avatarExistente == null) {
                    // Criar novo avatar
                    // ID será gerado pelo banco, então usamos 0 temporariamente
                    Avatar novoAvatar = new Avatar(
                            new AvatarId(0), // ID será gerado pelo banco
                            perfilIdSalvo
                    );
                    // Valores padrão já são definidos no construtor:
                    // - nivel = 1
                    // - experiencia = 0
                    // - dinheiro = 0
                    // - forca = 0
                    // - acessorios = lista vazia

                    // Salvar avatar
                    avatarRepository.save(novoAvatar);
                }
            }

            // Buscar o usuário salvo para obter o amizadeId
            Usuario usuarioSalvo = usuarioRepository.obter(new Email(request.email()));
            Integer amizadeId = usuarioSalvo != null && usuarioSalvo.getCodigoAmizade() != null 
                    ? usuarioSalvo.getCodigoAmizade().getCodigo() 
                    : null;

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegistroResponse(
                            true,
                            "Usuário registrado com sucesso",
                            request.email(),
                            perfilSalvo.getId() != null ? perfilSalvo.getId().getId() : null,
                            perfilSalvo.getUsername(),
                            amizadeId
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegistroResponse(false, "Dados inválidos: " + e.getMessage(), null, null, null, null));
        } catch (Exception e) {
            // Log da exceção completa para debug
            e.printStackTrace();
            String mensagemErro = e.getMessage();
            if (mensagemErro == null || mensagemErro.isEmpty()) {
                mensagemErro = e.getClass().getSimpleName() + ": " + 
                    (e.getCause() != null ? e.getCause().getMessage() : "Erro desconhecido");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegistroResponse(false, "Erro ao registrar usuário: " + mensagemErro, null, null, null, null));
        }
    }
}


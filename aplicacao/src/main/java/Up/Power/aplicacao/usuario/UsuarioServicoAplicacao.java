package Up.Power.aplicacao.usuario;

import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioRepository;
import Up.Power.perfil.PerfilRepository;
import Up.Power.perfil.PerfilService;
import Up.Power.perfil.PerfilId;
import Up.Power.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicoAplicacao implements UsuarioRepositorioAplicacao {

    private final UsuarioRepository repo;
    private final PerfilRepository perfilRepository;
    private final PerfilService perfilService;

    public UsuarioServicoAplicacao(UsuarioRepository repo, PerfilRepository perfilRepository, PerfilService perfilService) {
        this.repo = repo;
        this.perfilRepository = perfilRepository;
        this.perfilService = perfilService;
    }

    @Override
    public UsuarioResumo obterPorEmail(String email) {
        Usuario u = repo.obter(new Email(email));
        return u == null ? null : toResumo(u);
    }

    @Override
    public List<UsuarioResumo> listarAmigos(String email) {
        // Buscar o perfil do usuário pelo email
        Optional<Perfil> perfilOpt = perfilRepository.findByUsuarioEmail(email);
        if (perfilOpt.isEmpty()) {
            return List.of();
        }
        
        Perfil perfil = perfilOpt.get();
        PerfilId perfilId = perfil.getId();
        if (perfilId == null) {
            return List.of();
        }
        
        // Buscar amigos do perfil na tabela perfil_amigos
        List<Perfil> amigosPerfil = perfilService.listarAmigos(perfilId);
        
        // Converter perfis para UsuarioResumo
        return amigosPerfil.stream()
                .map(amigoPerfil -> {
                    // Buscar o usuário pelo email do perfil
                    Usuario usuario = repo.obter(amigoPerfil.getUsuarioEmail());
                    return usuario != null ? toResumo(usuario) : null;
                })
                .filter(resumo -> resumo != null)
                .collect(Collectors.toList());
    }

    public String removerAmizade(String email1, String email2) {
        // Buscar perfis pelos emails
        Optional<Perfil> perfil1Opt = perfilRepository.findByUsuarioEmail(email1);
        Optional<Perfil> perfil2Opt = perfilRepository.findByUsuarioEmail(email2);
        
        if (perfil1Opt.isEmpty() || perfil2Opt.isEmpty()) {
            throw new IllegalArgumentException("Um ou ambos os perfis não foram encontrados");
        }
        
        Perfil perfil1 = perfil1Opt.get();
        Perfil perfil2 = perfil2Opt.get();
        
        PerfilId perfilId1 = perfil1.getId();
        PerfilId perfilId2 = perfil2.getId();
        
        if (perfilId1 == null || perfilId2 == null) {
            throw new IllegalArgumentException("IDs de perfil inválidos");
        }
        
        perfilService.removerAmizade(perfilId1, perfilId2);
        return "Amizade removida com sucesso";
    }

    public boolean saoAmigos(String email1, String email2) {
        // Buscar perfis pelos emails
        Optional<Perfil> perfil1Opt = perfilRepository.findByUsuarioEmail(email1);
        Optional<Perfil> perfil2Opt = perfilRepository.findByUsuarioEmail(email2);
        
        if (perfil1Opt.isEmpty() || perfil2Opt.isEmpty()) {
            return false;
        }
        
        Perfil perfil1 = perfil1Opt.get();
        Perfil perfil2 = perfil2Opt.get();
        
        PerfilId perfilId1 = perfil1.getId();
        PerfilId perfilId2 = perfil2.getId();
        
        if (perfilId1 == null || perfilId2 == null) {
            return false;
        }
        
        return perfilService.saoAmigos(perfilId1, perfilId2);
    }

    public UsuarioResumo obterPorCodigoAmizade(int codigoAmizade, String emailExcluir) {
        List<Usuario> usuarios = repo.obterPorCodigoAmizade(codigoAmizade);
        // Retornar o primeiro usuário que não seja o próprio usuário
        return usuarios.stream()
                .filter(u -> !u.getUsuarioEmail().getCaracteres().equals(emailExcluir))
                .findFirst()
                .map(this::toResumo)
                .orElse(null);
    }

    public String adicionarAmigoPorCodigo(String emailRemetente, int codigoAmizade) {
        // Buscar o usuário que tem esse código de amizade (excluindo o próprio usuário)
        UsuarioResumo destinatarioResumo = obterPorCodigoAmizade(codigoAmizade, emailRemetente);
        if (destinatarioResumo == null) {
            throw new IllegalArgumentException("Nenhum usuário encontrado com esse código de amizade");
        }
        
        // Buscar perfis pelos emails
        Optional<Perfil> perfilRemetenteOpt = perfilRepository.findByUsuarioEmail(emailRemetente);
        Optional<Perfil> perfilDestinatarioOpt = perfilRepository.findByUsuarioEmail(destinatarioResumo.usuarioEmail());
        
        if (perfilRemetenteOpt.isEmpty() || perfilDestinatarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Perfis não encontrados");
        }
        
        Perfil perfilRemetente = perfilRemetenteOpt.get();
        Perfil perfilDestinatario = perfilDestinatarioOpt.get();
        
        PerfilId perfilIdRemetente = perfilRemetente.getId();
        PerfilId perfilIdDestinatario = perfilDestinatario.getId();
        
        if (perfilIdRemetente == null || perfilIdDestinatario == null) {
            throw new IllegalArgumentException("IDs de perfil inválidos");
        }
        
        // Verificar se já são amigos
        if (perfilService.saoAmigos(perfilIdRemetente, perfilIdDestinatario)) {
            throw new IllegalArgumentException("Vocês já são amigos");
        }
        
        // Adicionar amizade diretamente na tabela perfil_amigos
        perfilService.adicionarAmizade(perfilIdRemetente, perfilIdDestinatario);
        
        return "Amizade adicionada com sucesso";
    }

    private UsuarioResumo toResumo(Usuario u) {
        return new UsuarioResumo(
                u.getUsuarioEmail().getCaracteres(),
                u.getCodigoAmizade() != null ? u.getCodigoAmizade().getCodigo() : null,
                u.getNome(),
                u.getSenha(),
                u.getDataNascimento()
        );
    }
}
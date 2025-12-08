package Up.Power.aplicacao.usuario;

import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioRepository;
import Up.Power.usuario.AmizadeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServicoAplicacao implements UsuarioRepositorioAplicacao {

    private final UsuarioRepository repo;
    private final AmizadeService amizadeService;

    public UsuarioServicoAplicacao(UsuarioRepository repo, AmizadeService amizadeService) {
        this.repo = repo;
        this.amizadeService = amizadeService;
    }

    @Override
    public UsuarioResumo obterPorEmail(String email) {
        Usuario u = repo.obter(new Email(email));
        return u == null ? null : toResumo(u);
    }

    @Override
    public List<UsuarioResumo> listarAmigos(String email) {
        Usuario usuario = repo.obter(new Email(email));
        if (usuario == null) return List.of();

        if (usuario.getCodigoAmizade() == null) return List.of();

        var grupo = amizadeService.getAmizades().get(usuario.getCodigoAmizade());
        if (grupo == null) return List.of();

        return grupo.stream()
                .map(this::toResumo)
                .collect(Collectors.toList());
    }

    public String enviarConvite(String emailRemetente, String emailDestinatario) {
        Usuario remetente = repo.obter(new Email(emailRemetente));
        Usuario destinatario = repo.obter(new Email(emailDestinatario));

        return amizadeService.enviarConvite(remetente, destinatario);
    }

    public String aceitarConvite(String email1, String email2) {
        Usuario u1 = repo.obter(new Email(email1));
        Usuario u2 = repo.obter(new Email(email2));

        String resultado = amizadeService.aceitarConvite(u1, u2);

        repo.salvar(u1);
        repo.salvar(u2);

        return resultado;
    }

    public String removerAmizade(String email1, String email2) {
        Usuario u1 = repo.obter(new Email(email1));
        Usuario u2 = repo.obter(new Email(email2));

        String resp = amizadeService.removerAmizade(u1, u2);

        repo.salvar(u1);
        repo.salvar(u2);

        return resp;
    }

    public boolean saoAmigos(String email1, String email2) {
        Usuario u1 = repo.obter(new Email(email1));
        Usuario u2 = repo.obter(new Email(email2));

        return amizadeService.saoAmigos(u1, u2);
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
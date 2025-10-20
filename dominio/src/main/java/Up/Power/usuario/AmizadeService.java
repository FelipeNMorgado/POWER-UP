package Up.Power.usuario;

import Up.Power.AmizadeId;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioRepository;

import java.util.*;

public class AmizadeService {

    private final UsuarioRepository usuarioRepository;
    private final Map<AmizadeId, Set<Usuario>> amizades = new HashMap<>();

    public AmizadeService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String enviarConvite(Usuario remetente, Usuario destinatario) {
        if (remetente == null || destinatario == null) {
            throw new IllegalArgumentException("Usuários inválidos para convite");
        }
        return "Convite de amizade enviado";
    }

    public String aceitarConvite(Usuario usuario1, Usuario usuario2) {
        AmizadeId amizadeId = new AmizadeId(amizades.size() + 1);

        usuario1.setCodigoAmizade(amizadeId);
        usuario2.setCodigoAmizade(amizadeId);

        Set<Usuario> grupo = new HashSet<>(Set.of(usuario1, usuario2));
        amizades.put(amizadeId, grupo);

        return "Usuários agora são amigos";
    }

    public String recusarConvite() {
        return "Usuário recusou o pedido de amizade";
    }

    public String removerAmizade(Usuario usuario1, Usuario usuario2) {
        AmizadeId id = usuario1.getCodigoAmizade();
        if (id != null) {
            amizades.remove(id);
            usuario1.setCodigoAmizade(null);
            usuario2.setCodigoAmizade(null);
        }
        return "Usuários não são mais amigos";
    }

    public boolean saoAmigos(Usuario u1, Usuario u2) {
        AmizadeId id = u1.getCodigoAmizade();
        return id != null && amizades.containsKey(id) && amizades.get(id).contains(u2);
    }

    public Map<AmizadeId, Set<Usuario>> getAmizades() {
        return amizades;
    }

    public void salvarUsuario(Usuario u) {
        usuarioRepository.salvar(u);
    }
}
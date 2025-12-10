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
        
        // Buscar o maior amizadeId do banco para gerar o próximo
        int proximoId = usuarioRepository.obterProximoAmizadeId();
        AmizadeId amizadeId = new AmizadeId(proximoId);
        
        System.out.println("[DEBUG enviarConvite] Criando amizadeId: " + amizadeId.getCodigo());
        System.out.println("[DEBUG enviarConvite] Remetente: " + remetente.getUsuarioEmail().getCaracteres());
        System.out.println("[DEBUG enviarConvite] Destinatario: " + destinatario.getUsuarioEmail().getCaracteres());
        
        remetente.setCodigoAmizade(amizadeId);
        destinatario.setCodigoAmizade(amizadeId);
        
        System.out.println("[DEBUG enviarConvite] Remetente amizadeId após set: " + 
                          (remetente.getCodigoAmizade() != null ? remetente.getCodigoAmizade().getCodigo() : "null"));
        System.out.println("[DEBUG enviarConvite] Destinatario amizadeId após set: " + 
                          (destinatario.getCodigoAmizade() != null ? destinatario.getCodigoAmizade().getCodigo() : "null"));
        
        // Adicionar ambos ao grupo de amizade
        Set<Usuario> grupo = new HashSet<>(Set.of(remetente, destinatario));
        amizades.put(amizadeId, grupo);
        
        return "Usuários agora são amigos";
    }

    public String aceitarConvite(Usuario usuario1, Usuario usuario2) {
        AmizadeId amizadeId = new AmizadeId(amizades.size() + 1);

        usuario1.setCodigoAmizade(amizadeId);
        usuario2.setCodigoAmizade(amizadeId);

        Set<Usuario> grupo = new HashSet<>(Set.of(usuario1, usuario2));
        amizades.put(amizadeId, grupo);

        return "Usuários agora são amigos";
    }

    public String recusarConvite(Usuario usuario1, Usuario usuario2) {
        AmizadeId id = usuario1.getCodigoAmizade();
        if (id != null && amizades.containsKey(id)) {
            amizades.remove(id);
            usuario1.setCodigoAmizade(null);
            usuario2.setCodigoAmizade(null);
        }
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
        // Dois usuários são amigos se têm o mesmo amizadeId não nulo
        // A verificação é feita diretamente nos dados dos usuários, sem depender do mapa em memória
        AmizadeId id1 = u1.getCodigoAmizade();
        AmizadeId id2 = u2.getCodigoAmizade();
        
        // Log para debug
        System.out.println("[DEBUG saoAmigos] u1: " + u1.getUsuarioEmail().getCaracteres() + 
                          " amizadeId: " + (id1 != null ? id1.getCodigo() : "null"));
        System.out.println("[DEBUG saoAmigos] u2: " + u2.getUsuarioEmail().getCaracteres() + 
                          " amizadeId: " + (id2 != null ? id2.getCodigo() : "null"));
        
        // Ambos devem ter amizadeId não nulo
        if (id1 == null || id2 == null) {
            System.out.println("[DEBUG saoAmigos] Retornando false: um dos amizadeId é null");
            return false;
        }
        
        // Verificar se têm o mesmo amizadeId (comparando os códigos)
        boolean saoAmigos = id1.getCodigo() == id2.getCodigo();
        System.out.println("[DEBUG saoAmigos] Retornando: " + saoAmigos);
        return saoAmigos;
    }

    public Map<AmizadeId, Set<Usuario>> getAmizades() {
        return amizades;
    }

    public void salvarUsuario(Usuario u) {
        usuarioRepository.salvar(u);
    }
}
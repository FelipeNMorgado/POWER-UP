package Up.Power.usuario;

import Up.Power.Email;
import Up.Power.Usuario;

public interface UsuarioRepository {
    Usuario obter(Email usuarioEmail);
    void salvar(Usuario usuario);
}


